package tech.zdenek.jpromise;

public class UnexpectedResultException extends Exception
{
  private Object expected;
  private Object received;

  public UnexpectedResultException(Object expected, Object received)
  {
    this.expected = expected;
    this.received = received;
  }

  @Override
  public String getMessage()
  {
    return toString();
  }

  @Override
  public String toString()
  {
    return String.format("Received [%s]'%s' expected [%s]'%s'",
            received == null ? "?" : received.getClass().getSimpleName(), received,
            expected == null ? "?" : expected.getClass().getSimpleName(), expected);
  }
}
