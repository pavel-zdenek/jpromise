package tech.zdenek.jpromise;

import tech.zdenek.annotation.Nullable;

public interface HandlerValue<T>
{
  void handle(@Nullable T value);
}
