package tech.zdenek.jpromise;

import java.util.concurrent.Executor;

import tech.zdenek.annotation.NonNull;
import tech.zdenek.annotation.Nullable;

public class ResultPromise<T>
{
  @NonNull private final Executor resultExecutor;
  private @NonNull final Resolver<T> future;

  public ResultPromise(Executor resultExecutor)
  {
    this.resultExecutor = resultExecutor;
    this.future = new Resolver<>();
  }

  public ResultFuture<T> future() {
    return future;
  }

  public void resolve(@Nullable final T value)
  {
    resultExecutor.execute(new Runnable()
    {
      @Override
      public void run()
      {
        Exception exception = null;
        try {
          future.callDone(value);
        } catch (UnexpectedResultException e) {
          exception = e;
          future.callFailure(e);
        }
        future.callAlways(value, exception);
      }
    });
  }

  public void reject(@Nullable final Exception e)
  {
    resultExecutor.execute(new Runnable()
    {
      @Override
      public void run()
      {
        future.callFailure(e);
        future.callAlways(null, e);
      }
    });
  }
}
