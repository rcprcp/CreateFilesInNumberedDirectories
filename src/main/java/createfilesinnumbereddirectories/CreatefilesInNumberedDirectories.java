package createfilesinnumbereddirectories;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringJoiner;
import java.util.UUID;

public class CreatefilesInNumberedDirectories {

  // change these as required...
  private static final OutputTypes output = OutputTypes.JSON;
  private static final String DIRECTORY_NAME = "/Users/bob/blif/";
  private static final String SUB_DIR_PREFIX = "dir";
  private static final int NUM_DIRECTORIES = 10;
  private static final int FILES_PER_DIRECTORY = 5;
  private static final int LINES_PER_FILE = 10;
  private static final int COLUMNS_PER_LINE = 20;

  // The name says it all, but this was for the use case in Jdbc Producer where
  // the SDC Record's Fields must match the previous Record's Fields in order to
  // have this Record processed in the current database batch (JDBC addBatch and Execute).
  // if you enable this, every other Record will not match the previous Record, therefore
  // there is only one record in the JDBC addBatch. There is no benefit from the JDBC addBatch/execute code.
  // With this option set to true, and having many DB columns causes the absolute worst case
  // performance for JDBC Producer.
  private static final boolean ONE_FEWER_COLUMN_ON_EVERY_OTHER_RECORD = false;

  // Don't change these...
  private static final String JSON_FILENAME_SUFFIX = ".json";
  private static final String DELIMITED_DATA_FILENAME_SUFFIX = ".txt";
  private static final String DELIMITED_DATA_FIELD_SEPARATOR = "|";

  private CreatefilesInNumberedDirectories() {

  }

  public static void main(String... args) throws IOException {
    CreatefilesInNumberedDirectories cf = new CreatefilesInNumberedDirectories();
    cf.run();
  }

  private void run() throws IOException {

    long pkValue = 0;
    FileUtils.deleteDirectory(new File(DIRECTORY_NAME));
    FileUtils.forceMkdir(new File(DIRECTORY_NAME));

    for (int di = 0; di < NUM_DIRECTORIES; di++) {
      String dir = DIRECTORY_NAME + SUB_DIR_PREFIX + di;
      FileUtils.forceMkdir(new File(dir));

      String fn;

      for (int fc = 0; fc < FILES_PER_DIRECTORY; fc++) {
        switch (output) {
          case JSON:
            fn = dir + File.separator + UUID.randomUUID() + JSON_FILENAME_SUFFIX;
            try (PrintWriter pw = new PrintWriter(fn)) {
              for (int line = 1; line <= LINES_PER_FILE; line++) {
                StringJoiner join = new StringJoiner(",", "", "");
                for (int col = 1; col <= COLUMNS_PER_LINE; col++) {
                  if (ONE_FEWER_COLUMN_ON_EVERY_OTHER_RECORD && col == 2 && (line % 2) == 1) {
                    continue;
                  }
                  if (col == 1) {
                    join.add(String.format("\"c%d\": %d", col, pkValue));
                    ++pkValue;
                  } else {
                    join.add(String.format("\"c%d\": %d", col, col * line));
                  }
                }
                pw.println("{" + join.toString() + "}");
              }
            }
            break;

          case DELIMITED:
            fn = dir + File.separator + UUID.randomUUID() + DELIMITED_DATA_FILENAME_SUFFIX;
            StringJoiner j2 = new StringJoiner(DELIMITED_DATA_FIELD_SEPARATOR, "", "");
            try (PrintWriter pw = new PrintWriter(fn)) {
              for (int ll = 1; ll <= COLUMNS_PER_LINE; ll++) {
                j2.add(String.format("c%d", ll));
              }
              pw.println(j2.toString());

              for (int line = 1; line <= LINES_PER_FILE; line++) {
                j2 = new StringJoiner(DELIMITED_DATA_FIELD_SEPARATOR, "", "");
                for (int col = 1; col <= COLUMNS_PER_LINE; col++) {
                  if (ONE_FEWER_COLUMN_ON_EVERY_OTHER_RECORD && col == 1 && (line % 2) == 1) {
                    continue;
                  }
                  if (col == 1) {
                    j2.add("" + pkValue);
                    ++pkValue;
                  } else {
                    j2.add("" + (col * line));
                  }
                }
                pw.println(j2.toString());
              }
            }
            break;

          default:
            break;

        }
      }
    }
  }


  private enum OutputTypes {
    JSON, DELIMITED
  }
}
