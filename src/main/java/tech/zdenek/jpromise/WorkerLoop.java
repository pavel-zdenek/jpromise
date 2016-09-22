package tech.zdenek.jpromise;

import java.util.concurrent.Executor;

import tech.zdenek.annotation.NonNull;

public interface WorkerLoop
{
  @NonNull
  Executor createExecutor();
  void loop();
}
