public class Eser18
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
        if(ch == 'M' || ch=='m' ){
          state = 1;
        }else  if(ch != 'M' || ch != 'm'){
          state = 6;
        }
		    break;

	    case 1:
        if(ch == 'I' || ch=='i' ){
          state = 2;
        }else  if(ch != 'I' || ch != 'i'){
          state = 7;
        }
		    break;

	    case 2:
        if(ch == 'N' || ch=='n' ){
          state = 3;
        }else  if(ch != 'N' || ch != 'n'){
          state = 8;
        }
		    break;

      case 3:
        if(ch == 'O' || ch=='o' ){
          state = 4;
        }else  if(ch != 'O' || ch != 'o'){
          state = 9;
        }
        break;

        case 4:
          if(ch == 'I' || ch=='i' ){
            state = 5;
          }else  if(ch != 'I' || ch != 'i'){
            state = 5;
          }
          break;

        case 6:
          if(ch == 'I' || ch=='i' ){
            state = 7;
          }else  if(ch != 'I' || ch != 'i'){
            state = -1;
          }
          break;

        case 7:
          if(ch == 'N' || ch=='n' ){
            state = 8;
          }else  if(ch != 'N' || ch != 'n'){
            state = -1;
          }
          break;

        case 8:
          if(ch == 'O' || ch=='o' ){
            state = 5;
          }else  if(ch != 'O' || ch != 'o'){
            state = -1;
          }
          break;

        case 9:
          if(ch == 'I' || ch=='i' ){
            state = 5;
          }else  if(ch != 'I' || ch != 'i'){
            state = -1;
          }
          break;
	    }
	}
	return state == 5;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
