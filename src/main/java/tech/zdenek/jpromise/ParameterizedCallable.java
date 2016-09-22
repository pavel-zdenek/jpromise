package tech.zdenek.jpromise;

import tech.zdenek.annotation.NonNull;
import tech.zdenek.annotation.Nullable;

public interface ParameterizedCallable<Param, Retval>
{
  @Nullable
  Retval call(@NonNull Param param) throws Exception;
}
