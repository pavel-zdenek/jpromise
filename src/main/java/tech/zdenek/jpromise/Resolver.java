package tech.zdenek.jpromise;

import tech.zdenek.annotation.NonNull;
import tech.zdenek.annotation.Nullable;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Resolver<T> implements ResultFuture<T>
{
  private class BlockingResult<T>
  {
    private final Lock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();

    private Boolean success = null;
    private T value;
    private Exception exception;

    public void fail(Exception exception)
    {
      lock.lock();
      success = false;
      this.exception = exception;
      cond.signal();
      lock.unlock();
    }

    public void success(T value)
    {
      lock.lock();
      success = true;
      this.value = value;
      cond.signal();
      lock.unlock();
    }

    public T get() throws Exception
    {
      lock.lock();
      if(success == null) {
        cond.await();
      }
      try {
        if(success) {
          return value;
        } else {
          throw exception;
        }
      } finally {
        lock.unlock();
      }
    }
  }
  private final BlockingResult<T> blockingResult = new BlockingResult<T>();

  @NonNull
  private HandlerValue<T> doneCallback = new HandlerValue<T>()
  {
    @Override
    public void handle(@Nullable T value)
    {
      blockingResult.success(value);
    }
  };
  @NonNull
  private HandlerException failCallback = new HandlerException()
  {
    @Override
    public void handle(@NonNull Exception e)
    {
      blockingResult.fail(e);
    }
  };
  @Nullable
  private HandlerAny<T> alwaysCallback;

  public void callDone(T value) throws UnexpectedResultException
  {
    doneCallback.handle(value);
  }

  public void callFailure(Exception e)
  {
    failCallback.handle(e);
  }

  public void callAlways(T value, Exception e)
  {
    if(alwaysCallback != null)
    {
      alwaysCallback.handle(value, e);
    }
  }

  @NonNull
  @Override
  public ResultFuture<T> done(final @NonNull HandlerValue<T> handler)
  {
    final HandlerValue<T> original = doneCallback;
    doneCallback = new HandlerValue<T>()
    {
      @Override
      public void handle(@Nullable T value)
      {
        handler.handle(value);
        original.handle(value);
      }
    };
    return this;
  }

  @NonNull
  @Override
  public ResultFuture<T> fail(final @NonNull HandlerException handler)
  {
    final HandlerException original = failCallback;
    failCallback = new HandlerException()
    {
      @Override
      public void handle(@NonNull Exception e)
      {
        handler.handle(e);
        original.handle(e);
      }
    };
    return this;
  }

  @NonNull
  @Override
  public ResultFuture<T> always(@Nullable HandlerAny<T> handler)
  {
    this.alwaysCallback = handler;
    return this;
  }

  @Nullable
  @Override
  public T blockingGet() throws Exception
  {
    return blockingResult.get();
  }
}
