# Create Files In Numbered Directories

This program is convenient for testing issues with S3 buckets, particularly where you need to process the file metadata, so having many small files is ok. 

Every parameter this program uses is in the source code - there are no command line arguments. 

The output files can contain delimited records, or the files can contain records in JSON format.  When the output is in JSON format, every line is a simple JSON document. See http://jsonlines.org/ for more information about the idea of single line JSON format.

This program can run directly from inside IntelliJ (or other IDE) 

Or you can create a fat jar - 

```
mvn clean package
```