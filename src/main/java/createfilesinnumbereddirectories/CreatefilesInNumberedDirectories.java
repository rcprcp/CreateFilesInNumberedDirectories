package createfilesinnumbereddirectories;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringJoiner;
import java.util.UUID;

public class CreatefilesInNumberedDirectories {

  private static final String DIRECTORY_NAME = "/Users/bob/input";
  private static final String SUB_DIR_PREFIX = "/dir";
  private static final String SUFFIX = ".json";
  private static final String DELIM = "/";

  private static final int NUM_DIRECTORIES = 20;
  private static final int FILES_PER_DIRECTORY = 50;
  private static final int LINES_PER_FILE = 10000;
  private static final int COLUMNS_PER_LINE = 400;
  private enum OutputTypes {
    JSON, DELIMITED, TEXT
  }

  public static void main(String... args) throws IOException {
    CreatefilesInNumberedDirectories cf = new CreatefilesInNumberedDirectories();
    cf.run(DIRECTORY_NAME,
        NUM_DIRECTORIES,
        FILES_PER_DIRECTORY,
        LINES_PER_FILE,
        COLUMNS_PER_LINE,
        OutputTypes.JSON);
  }

  private CreatefilesInNumberedDirectories() {

  }

  private void run(
      String directoryName,
      int dirCount,
      int filesPerDirectory,
      int linesPerFile,
      int columnsPerLine,
      OutputTypes type
      ) throws IOException {

    FileUtils.deleteDirectory(new File(DIRECTORY_NAME));
    FileUtils.forceMkdir(new File(DIRECTORY_NAME));

    for (int di = 0; di < dirCount; di++) {
      String dir = DIRECTORY_NAME + SUB_DIR_PREFIX + di;
      FileUtils.forceMkdir(new File(dir));

      for (int fc = 0; fc < filesPerDirectory; fc++) {
        String fn = dir + DELIM  + UUID.randomUUID() + SUFFIX;
        try (PrintWriter pw = new PrintWriter(fn)) {
          for (int i = 1; i < LINES_PER_FILE; i++) {
            switch(type) {
              case JSON:
                StringJoiner join = new StringJoiner(",", "", "");
                for(int j = 1; j <= COLUMNS_PER_LINE ;j++) {
                  join.add(String.format("\"c%d\": %d", j, j * i));
                }
                pw.println("{" + join.toString() + "}");
                break;

              case DELIMITED:
                StringJoiner j2 = new StringJoiner(",", "", "");
                for(int j = 1; j <= COLUMNS_PER_LINE ;j++) {
                  j2.add(String.format("\"c%d\"|%d", j, j * i));
                }
                pw.println(j2.toString());

                break;

              case TEXT:
                pw.println("text format.  directory " + di + " filenum " + fc + " line " + i);
                break;
            }
          }
        }
      }
    }
  }
}
