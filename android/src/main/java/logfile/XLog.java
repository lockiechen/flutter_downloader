package logfile;
 import com.elvishew.xlog.LogConfiguration;
 import com.elvishew.xlog.LogLevel;
 import com.elvishew.xlog.XLog;
 import com.elvishew.xlog.flattener.ClassicFlattener;
 import com.elvishew.xlog.libcat.LibCat;
 import com.elvishew.xlog.printer.Printer;
 import com.elvishew.xlog.printer.file.FilePrinter;
 import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
 import com.elvishew.xlog.printer.file.writer.SimpleWriter;

public class XLog {
    private static final long MAX_TIME = 1000 * 60 * 60 * 24 * 2; // two days
    public static final void init() {
        LogConfiguration config = new LogConfiguration.Builder()
            .logLevel(LogLevel.ALL)
            .tag(getString(R.string.global_tag))                   // Specify TAG, default: "X-LOG"
            // .enableThreadInfo()                                 // Enable thread info, disabled by default
            // .enableStackTrace(2)                                // Enable stack trace info with depth 2, disabled by default
            // .enableBorder()                                     // Enable border, disabled by default
            // .jsonFormatter(new MyJsonFormatter())               // Default: DefaultJsonFormatter
            // .xmlFormatter(new MyXmlFormatter())                 // Default: DefaultXmlFormatter
            // .throwableFormatter(new MyThrowableFormatter())     // Default: DefaultThrowableFormatter
            // .threadFormatter(new MyThreadFormatter())           // Default: DefaultThreadFormatter
            // .stackTraceFormatter(new MyStackTraceFormatter())   // Default: DefaultStackTraceFormatter
            // .borderFormatter(new MyBoardFormatter())            // Default: DefaultBorderFormatter
            // .addObjectFormatter(AnyClass.class,                 // Add formatter for specific class of object
            //     new AnyClassObjectFormatter())                  // Use Object.toString() by default
            // .addInterceptor(new BlacklistTagsFilterInterceptor(    // Add blacklist tags filter
            //     "blacklist1", "blacklist2", "blacklist3"))
            // .addInterceptor(new WhitelistTagsFilterInterceptor( // Add whitelist tags filter
            //     "whitelist1", "whitelist2", "whitelist3"))
            // .addInterceptor(new MyInterceptor())                // Add a log interceptor
            .build();


                
            Printer filePrinter = new FilePrinter                      // Printer that print the log to the file system
                .Builder(new File(getExternalCacheDir().getAbsolutePath(), "devops_app_log").getPath())       // Specify the path to save log file
                .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
                // .backupStrategy(new MyBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
                .cleanStrategy(new FileLastModifiedCleanStrategy(MAX_TIME))     // Default: NeverCleanStrategy()
                .flattener(new ClassicFlattener())                           // Default: DefaultFlattener
                .writer(new SimpleWriter() {                           // Default: SimpleWriter
                    @Override
                    public void onNewFileCreated(File file) {
                      super.onNewFileCreated(file);
                      final String header = "\n>>>>>>>>>>>>>>>> File Header >>>>>>>>>>>>>>>>" +
                              "\nDevice Manufacturer: " + Build.MANUFACTURER +
                              "\nDevice Model       : " + Build.MODEL +
                              "\nAndroid Version    : " + Build.VERSION.RELEASE +
                              "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
                              "\nApp VersionName    : " + BuildConfig.VERSION_NAME +
                              "\nApp VersionCode    : " + BuildConfig.VERSION_CODE +
                              "\n<<<<<<<<<<<<<<<< File Header <<<<<<<<<<<<<<<<\n\n";
                      appendLog(header);
                    }
                  })
                .build();

            XLog.init(config, filePrinter);
            LibCat.config(true, filePrinter);

    }
}
