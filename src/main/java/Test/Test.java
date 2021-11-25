package Test;

import java.io.File;

public class Test {
    static class saveTest{
        public static void main(String[] args){
            String path = "Data\\model";
            File file = new File(path);
            file.mkdir();
        }
    }

    static class databaseTest{
        public static void main(String[] args){

        }
    }
}
