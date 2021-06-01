public class Eser16 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            char ch = s.charAt(i);
            switch (state) {
            case 0:
                if(ch == 'a'){
                    state = 4;
                }else if(ch == 'b'){
                    state = 1;
                }else  state = 3;
                break;

            case 1:
                if(ch == 'a'){
                    state = 4;
                }else if(ch == 'b'){
                    state = 2;
                }else 
                    state = 3;
                break;

            case 2:
                if(ch == 'a'){
                    state = 4;
                }else 
                    state = 3;
                
                break;

            }
            i = i + 1;
        }
        return (state == 4);
    }

    public static void main(String[] args) {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}