import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.locks.LockSupport;

public class Main {

    public static void main(String[] args) throws Exception {

        /* Инстанцирование интерфейса Worker для проверки работы метода */
        Worker worker = new SomeClass();
        worker.doWork();

        /* Стринги класса SomeClass, необходимые для сборки итогового класса */
        String workerClassStringStart = "public class SomeClass implements Worker {\n" +
                                        "    public SomeClass(){\n" +
                                        "    }\n" +
                                        "\n" +
                                        "    public void doWork(){";

        String workerClassStringEnd = "    }\n" +
                                      "    static { \n" +
                                      "        System.out.println(\"Игорь\");\n" +
                                      "    }\n" +
                                      "}";

        /* Добавление считанных строк в тело метода public void doWork() */
        String givenMethodCode = linesCodeReader();
        String givenClass = workerClassStringStart + givenMethodCode + workerClassStringEnd;
        codeStringWriter(givenClass);

        /* Создание файла SomeClass.java и его компилирование в файл SomeClass.class */
        givenFileCompiler();

        /* Подгрузка и исполнение метода кастомного загрузчика в рантайме */
        while (true) {
            LockSupport.parkNanos(3_000_000_000L);
            useCustomClassLoader();
        }
    }

        /* Метод построчного считывания код метода doWork() */
        private static String linesCodeReader(){
            Scanner reader = new Scanner(System.in);
            System.out.println("Введите строку кода метода doWork: ");
            String givenInput = "";
            String givenMethod = "      ";
            while (!(givenInput = reader.nextLine()).equals("")) {
                givenMethod = givenMethod + givenInput + "\n";
            }
            System.out.println("Код метода doWork: " + "\n" + givenMethod);
            return givenMethod;
        }

        /* Метод создания .txt файла с кодом, который введен пользователем через консоль */
        public static void codeStringWriter(String givenStringCode) throws IOException {
            String str = givenStringCode;
            FileOutputStream outputStream = new FileOutputStream("fileWithCode.txt");
            byte[] strToBytes = str.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        }

    /* Метод создания .java файла из .txt и компиляции в .class */
    public static void givenFileCompiler() throws IOException {
        try {
            System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_202");
            BufferedReader br = new BufferedReader(new FileReader("fileWithCode.txt"));
            PrintWriter writer = new PrintWriter("SomeClass.java");
            String reader = "";
            System.out.println("start");
            while((reader = (br.readLine()))!= null){
                writer.write(reader);
            }
            br.close();
            writer.close();
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            System.out.println(compiler);
            compiler.run(null, null, null,"SomeClass.java");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

        /* Метод загрузки класса и вызова его метода */
        private static void useCustomClassLoader() throws Exception {
            ClassLoader cl = new MyClassLoader();
            Class<?> loadingWorker = cl.loadClass("SomeClass");
            Worker loadingInterfaceWorker = (Worker)loadingWorker.newInstance();
            loadingInterfaceWorker.doWork();
        }
    }
