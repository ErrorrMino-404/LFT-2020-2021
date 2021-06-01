public class Eser13
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
        if(ch >= 48 && ch <= 57){
          if(ch % 2 == 0){
            state = 1;
          }else {
            state = 2;
          }
        }else
          state = -1;
		    break;

	    case 1:
        if(ch >= 48 && ch <= 57){
          if(ch % 2 == 0){
            state = 1;
          }else {state = 2;}
        }else if ( ch >= 65 && ch <= 75){
          state = 3;
        }else state = -1;

		    break;

	    case 2:
        if (ch >= 48 && ch <= 57){
          if(ch % 2 == 0){
            state = 1;
          }else {state = 2;}
        }else if(ch > 75 && ch <=90){
          state = 4;
        }else
          state = -1;
		    break;
        // state = 3 --> T3 pari A...K
        // state = 4 --> T4 dispari L...Z
	}
    }	return state == 3 || state == 4;
}
    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
