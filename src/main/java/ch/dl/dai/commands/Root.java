package ch.dl.dai.commands;

import picocli.CommandLine;

@CommandLine.Command(
    description = "CLI to apply basic filters on images",
    version = "1.0.0",
    subcommands = {
      ApplyFilter.class,
    },
    scope = CommandLine.ScopeType.INHERIT,
    mixinStandardHelpOptions = true)
public class Root {

  static final int MAX_ARG = 4;

  public enum AvailableFilter {
    SEPIA,
    GRAYSCALE,
    MOVING_AVERAGE,
    COLOR_INTENSITY,
    ADJUST_COLOR
  }

  public enum SupportedImageType {
    BMP,
  }

  @CommandLine.Parameters(index = "0", description = "The file path of the image.")
  String filepath;

  @CommandLine.Parameters(index = "1", description = "The file path of the output image.")
  String outputFilepath;

  @CommandLine.Parameters(
      index = "2",
      description = "Parameters of the filter (the required amount depends on the chosen filter)",
      arity = "0.." + MAX_ARG,
      defaultValue = CommandLine.Parameters.NULL_VALUE,
      hideParamSyntax = true,
      type = int.class)
  int[] p = new int[MAX_ARG]; // Short name chosen for CLI legibility

  @CommandLine.Option(
      names = {"-f", "--filter"},
      description = "The filter to apply on the image (possible values: ${COMPLETION-CANDIDATES}).",
      required = true)
  AvailableFilter filter;

  public SupportedImageType getImageType() throws RuntimeException {
    String fileExtension = filepath.substring(filepath.lastIndexOf('.') + 1);
    if (fileExtension.equalsIgnoreCase("bmp")) return SupportedImageType.BMP;
    else throw new RuntimeException("Unsupported file (" + fileExtension.toUpperCase() + ").");
  }

  public String getOutputFilepath() {
    return outputFilepath;
  }

  public AvailableFilter getFilter() {
    return filter;
  }

  public String getFilepath() {
    return filepath;
  }

  public int[] getParameters() {
    return p;
  }
}
