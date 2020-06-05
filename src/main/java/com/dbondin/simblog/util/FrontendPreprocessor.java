package com.dbondin.simblog.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrontendPreprocessor {

  public static enum MODE {
    ONELINE,
    MULTILINE
  }
  
  public FrontendPreprocessor(final MODE mode) {
    this.mode = mode;
  }
  
  private static final class ExtensionFilenameFilter implements FilenameFilter {

    private List<String> dotExtensions;

    public ExtensionFilenameFilter(final Collection<String> extensions) {
      this.dotExtensions = extensions.stream().map(extension -> "." + extension).collect(Collectors.toList());
    }

    public ExtensionFilenameFilter(final String extension) {
      this(Collections.singletonList(extension));
    }

    @Override
    public boolean accept(File dir, String name) {
      boolean result = false;
      if (new File(dir, name).isFile()) {
        final String lcName = name.toLowerCase();
        result = dotExtensions.stream().anyMatch(extension -> lcName.endsWith(extension));
      }
      return result;
    }
  }

  private static final ExtensionFilenameFilter SRC_FILENAME_FILTER = new ExtensionFilenameFilter("tmpl.js");
  
  private static final Pattern PATTERN_INCLUDE = Pattern.compile("^(\\s*)#include\\s+([\\S]+)$");

  private MODE mode;
 
  public static void main(String[] args) {
    FrontendPreprocessor fp = new FrontendPreprocessor(MODE.MULTILINE);
    fp.includeFile("/home/dbondin/vue-test/vue-test.x-template", System.out, "    ");
    FrontendPreprocessor fp2 = new FrontendPreprocessor(MODE.ONELINE);
    fp2.includeFile("/home/dbondin/vue-test/vue-test.x-template", System.out, "    ");
  }
  
  public static void main_(String[] args) {

    if (args.length != 2) {
      log.error("Usage: FrontendPreprocessor srcDir destFile ONELINE|MULTILINE");
      System.exit(1);
    }

    final FrontendPreprocessor frontendPreprocessor = new FrontendPreprocessor(MODE.valueOf(args[2]));

    frontendPreprocessor.processDir(args[0], args[1]);

  }

  private void processDir(final String srcDir, final String destFile) {

    final File fSrcDir = new File(srcDir);

    if (!fSrcDir.isDirectory()) {
      throw new RuntimeException("Not a directory");
    }

    processFiles(Arrays.asList(fSrcDir.list(SRC_FILENAME_FILTER)), destFile);
  }

  private void includeFile(final String filename, final OutputStream os, final String prefix) {
    try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
      os.write(prefix.getBytes());
      os.write("/* Testing */\r\n".getBytes());
      if(mode == MODE.ONELINE) {
        os.write(prefix.getBytes());
        os.write('\'');
      }
      int lineNo=0;
      while (true) {
        final String line = in.readLine();
        if (line == null) {
          os.write('\'');
          os.write("\r\n".getBytes());
          os.flush();
          break;
        }
        if(mode == MODE.MULTILINE) {
          if(lineNo != 0) {
            os.write("\' +".getBytes());
            os.write("\r\n".getBytes());
          }
          os.write(prefix.getBytes());
          os.write('\'');
        }
        os.write(escapeString(line).getBytes());
        ++lineNo;
      }
      if(mode == MODE.ONELINE) {
        os.write('\'');
      }
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(ex);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  private void processFile(final String srcFile, final OutputStream os) {
    try (BufferedReader in = new BufferedReader(new FileReader(srcFile))) {
      while (true) {
        final String line = in.readLine();
        if (line == null) {
          break;
        }
        final Matcher m = PATTERN_INCLUDE.matcher(line);
        if(m.matches()) {
          final String offsetString = m.group(1);
          final String includeFilename = m.group(2);
        }
      }
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(ex);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private void processFiles(final List<String> srcFiles, final String destFile) {
    try (FileOutputStream fos = new FileOutputStream(destFile)) {
      srcFiles.forEach(srcFile -> processFile(srcFile, fos));
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(ex);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  private static final Pattern LEADING_SPACES_PATTERN = Pattern.compile("^\\s+(.*)$");
  private static final Pattern FINISHING_SPACES_PATTERN = Pattern.compile("^(.*\\S)\\s+$");
  
  private String escapeString(final String str) {
    String result = str.replace("'", "\\'");
    final Matcher m = LEADING_SPACES_PATTERN.matcher(result);
    if(m.matches()) {
      result = m.group(1);
    }
    final Matcher m2 = FINISHING_SPACES_PATTERN.matcher(result);
    if(m2.matches()) {
      result = m2.group(1);
    }
    if(!result.startsWith("<")) {
      result = " " + result;
    }
    return result;
  }
}
