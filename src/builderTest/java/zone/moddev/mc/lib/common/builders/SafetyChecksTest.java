package zone.moddev.mc.lib.common.builders;

import com.google.gson.JsonParser;

import zone.moddev.mc.lib.util.parse.JParse;
import zone.moddev.mc.lib.util.parse.JParseException;

/** Regressions for malformed numeric input; overflow must never turn it into valid settings. */
public final class SafetyChecksTest {
  public static void main(String[] args) {
    if (JParse.begin(JsonParser.parseString("2147483647")).intValue().getAsInt() != Integer.MAX_VALUE)
      throw new AssertionError("Maximum int");
    if (JParse.begin(JsonParser.parseString("-9223372036854775808")).longValue().getAsLong()
        != Long.MIN_VALUE)
      throw new AssertionError("Minimum long");
    rejects(() -> JParse.begin(JsonParser.parseString("4294967297")).intValue().range(0, 10));
    rejects(() -> JParse.begin(JsonParser.parseString("1.5")).intValue().getAsInt());
    rejects(
        () -> JParse.begin(JsonParser.parseString("18446744073709551617")).longValue().getAsLong());
    rejects(() -> JParse.begin(JsonParser.parseString("1e1000")).doubleValue().min(0));
    rejects(() -> JParse.begin(JsonParser.parseString("1e100")).floatValue().range(0, 10));
    rejects(() -> JParse.begin((com.google.gson.JsonElement) null).obj());
    System.out.println(
        "Numeric overflow, fractional integers, nonfinite values, and null JSON checks passed");
  }

  private static void rejects(Runnable run) {
    try {
      run.run();
    } catch (JParseException expected) {
      return;
    }
    throw new AssertionError("Invalid input was accepted");
  }
}
