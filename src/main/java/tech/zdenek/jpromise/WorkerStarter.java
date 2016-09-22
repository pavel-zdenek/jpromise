package tech.zdenek.jpromise;

import java.util.concurrent.Executor;

import tech.zdenek.annotation.NonNull;
import tech.zdenek.annotation.Nullable;

public class WorkerStarter implements Runnable
{
  @NonNull
  private final Executor callbackExecutor;
  @Nullable private final Runnable startedCallback;
  @NonNull private WorkerLoop selfWorker;
  @NonNull private Executor selfExecutor;

  public WorkerStarter(@NonNull WorkerLoop worker, @Nullable Executor callbackExecutor, @Nullable Runnable startedCallback)
  {
    this.selfWorker = worker;
    this.callbackExecutor = callbackExecutor;
    this.startedCallback = startedCallback;
  }

  @Override
  public void run()
  {
    prepareThreadContext();
    selfExecutor = selfWorker.createExecutor();
    if(callbackExecutor != null && startedCallback != null)
    {
      callbackExecutor.execute(startedCallback);
    }
    selfWorker.loop();
  }

  protected void prepareThreadContext() {}

  public <Param, Retval> ResultFuture<Retval> postTask(
          final Param parameter,
          final ParameterizedCallable<Param, Retval> task)
  {
    final ResultPromise<Retval> promise = new ResultPromise<>(callbackExecutor);
    selfExecutor.execute(new Runnable()
    {
      @Override
      public void run()
      {
        try {
          Retval retval = task.call(parameter);
          promise.resolve(retval);
        } catch(Exception e) {
          promise.reject(e);
        }
      }
    });
    return promise.future();
  }
}
