package jdbc.basic.launch;

public class Constants {
   public final static int CREATE_TABLE = 1;
   public final static int INSERT_DATA = 2;
   public final static int READ_DATA = 3;
   public final static int UPDATE_DATA = 4;
   public final static int DELETE_DATA = 5;
   public final static int EXIT = 9;
   public final static int MAX_ATTEMPTS = 3;
   public final static int MIN_ATTEMPTS = 0;
   public final static int MIN_OPTION = CREATE_TABLE;
   public final static int MAX_OPTION =DELETE_DATA;
   public final static int SUCCESS = CREATE_TABLE;
   public final static int FAILED = MIN_ATTEMPTS;
   public final static int ERROR = -1;
 
}
