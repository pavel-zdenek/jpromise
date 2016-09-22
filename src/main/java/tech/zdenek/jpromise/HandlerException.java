package tech.zdenek.jpromise;

import tech.zdenek.annotation.NonNull;

public interface HandlerException
{
  void handle(@NonNull Exception e);
}
