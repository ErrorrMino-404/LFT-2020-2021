public class Eser14
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
          if(ch % 2 != 0){
            state = 2;
          }else {
              state = 1;
          }
        }else if(ch == ' '){
          state = 5;
        }
        else {
          state = -1;
        }
		    break;

	    case 1:
      System.out.println("aono dentro al case 1");
        if(ch >= 48 && ch <= 57){ //se il valore è pari, rimane in state = 1
          if(ch % 2 != 0){
            state = 2;
          } else {
            state = 1;
          }
        }else if(ch >= 'A' && ch <= 'K' || ch >= 'a' && ch <= 'z' ){
          state = 3;
        }else if (ch == ' '){
          System.out.println("devo andare al case 5");
          state = 5;
        }

		    break;

	    case 2: // case stati dispari
        if(ch >= 48 && ch <= 57){
          if(ch % 2 != 0){
            state = 2;
          }else {state = 1;}
        }else if(ch >= 'L' && ch <= 'Z' || ch >= 'a' && ch <= 'z') {
          state = 4;
        }else if(ch == ' '){
          state = 6;
        }else {
          state = -1;
        }
		    break;
        // state = 3 --> T3 pari A...K
        // state = 4 --> T4 dispari L...Z
      case 3:
        if(ch >= 'a' && ch <= 'z'){
          state = 3;
        }else {
          state = -1;
        }

      break;

      case 4:
        if(ch >= 'a' && ch <= 'z'){
          state = 4;
        }else {
          state = -1;
        }

      break;

      case 5: //spazi dopo la matricola pari
        if(ch == ' '){
          state = 5;
        }else if(ch >= 'A' && ch <= 'K'){
          state = 3;
        }else {
          state = -1;
        }

      break;

      case 6: // spazi dopo la matricola dispari
        if(ch == ' '){
          state = 6;
        }else if(ch >= 'L' && ch <= 'Z'){
          state = 4;
        }else {
          state = -1;
        }

      break;
	}
    }	return state == 3 || state == 4;
}
    public static void main(String[] args)
    {
	       System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
