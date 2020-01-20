package createfilesinnumbereddirectories;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class CreatefilesInNumberedDirectories {

  private static final String DIRECTORY_NAME = "/Users/bob/test88";
  private static final String SUB_DIR_PREFIX = "/dir";
  private static final String SUFFIX = ".tmp";
  private static final String DELIM = "/";

  private static final int NUM_DIRECTORIES = 500;
  private static final int FILES_PER_DIRCTORY = 300;
  private static final int LINES_PER_FILE = 100;
  private static boolean OUTPUT_JSON = true;
  private int dirCount;
  private int filesPerDirectory;

  public static void main(String... args) throws IOException {
    CreatefilesInNumberedDirectories cf = new CreatefilesInNumberedDirectories(DIRECTORY_NAME,
        NUM_DIRECTORIES,
        FILES_PER_DIRCTORY
    );
    cf.run();
  }

  private CreatefilesInNumberedDirectories(String directoryName, int dirCount, int filesPerDirectory) {
    this.dirCount = dirCount;
    this.filesPerDirectory = filesPerDirectory;
  }

  private void run() throws IOException {
    FileUtils.deleteDirectory(new File(DIRECTORY_NAME));
    FileUtils.forceMkdir(new File(DIRECTORY_NAME));

    for (int di = 0; di < dirCount; di++) {
      String dir = DIRECTORY_NAME + SUB_DIR_PREFIX + di;
      FileUtils.forceMkdir(new File(dir));

      for (int fc = 0; fc < filesPerDirectory; fc++) {
        String fn = dir + DELIM  + UUID.randomUUID() + SUFFIX;
        try (PrintWriter pw = new PrintWriter(fn)) {
          for (int i = 0; i < LINES_PER_FILE; i++) {
            if (OUTPUT_JSON) {
              pw.println("{\"id\": " + i + ", \"fname\": \"" + UUID.randomUUID() + "\", \"lname\": \"" + UUID.randomUUID() + "\"}");
            } else {
              pw.println("text format.  directory " + di + " filenum " + fc + " line " + i);
            }
          }
        }
      }
    }
  }
}
