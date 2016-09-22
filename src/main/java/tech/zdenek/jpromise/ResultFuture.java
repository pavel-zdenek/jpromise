package tech.zdenek.jpromise;

import tech.zdenek.annotation.NonNull;
import tech.zdenek.annotation.Nullable;

public interface ResultFuture<T>
{
  @NonNull
  ResultFuture<T> done(@NonNull HandlerValue<T> handler);
  @NonNull
  ResultFuture<T> fail(@NonNull HandlerException handler);
  @NonNull
  ResultFuture<T> always(@Nullable HandlerAny<T> handler);
  @Nullable
  T blockingGet() throws Exception;
}
