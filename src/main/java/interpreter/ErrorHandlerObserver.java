package interpreter;

import org.example.Result;
import org.example.observer.Observer;

public class ErrorHandlerObserver implements Observer<Result> {
  private final ErrorHandler handler;

  public ErrorHandlerObserver(ErrorHandler handler) {
    this.handler = handler;
  }

  @Override
  public void notifyChange(Result result) {
    if(!result.isSuccessful()) {
      if (result.errorMessage().equals("No tokens provided")) return;
      handler.reportError(result.errorMessage());
    }
  }
}
