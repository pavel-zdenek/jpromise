package tech.zdenek.jpromise;

import tech.zdenek.annotation.Nullable;

public interface HandlerAny<T>
{
  void handle(@Nullable T value, @Nullable Exception e);
}
