package adapters;

import configurations.ConfigLoader;
import configurations.Configuration;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PrintScriptLinterImplementation implements PrintScriptLinter {
  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
    if (version.equals("1.1")){
      throw new UnsupportedOperationException("Version 1.1 is not supported");
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(src));
    ConfigLoader configLoader = new ConfigLoader();
    Configuration configuration = configLoader.load(config);
    int lenght = 0;
    try {
      lenght = src.available();
    }
    catch (Exception e){
      e.printStackTrace();
    }


  }
}
