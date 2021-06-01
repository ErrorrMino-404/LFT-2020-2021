public class Eser15 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            char ch = s.charAt(i);
            switch (state) {
            case 0:
                if ((ch >= 'A' && ch <= 'K')) {
                    state = 1;
                } else if ((ch >= 'L' && ch <= 'Z')) {
                    state = 2;
                } else {
                    state = -1;
                }
                break;

            case 1:
                if (ch >= 48 && ch <= 57) {
                    if (ch % 2 != 0) {
                        state = 3;
                    } else {
                        state = 4;
                    }
                } else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                    state = 1;
                } else {
                    state = -1;
                }
                break;

            case 2:
                if (ch >= 48 && ch <= 57) {
                    if (ch % 2 != 0) {
                        state = 5;
                    } else {
                        state = 6;
                    }
                } else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                    state = 2;
                } else {
                    state = -1;
                }
                break;

            case 3: // stato non finale del T2, , nomi a_k
                if (ch >= 48 && ch <= 57) {
                    if (ch % 2 != 0) {
                        state = 3;
                    } else {
                        state = 4;
                    }
                } else {
                    state = -1;
                }
                break;

            case 4: // stato finale del T2, nomi a_k
                if (ch >= 48 && ch <= 57) {
                    if (ch % 2 != 0) {
                        state = 3;
                    } else {
                        state = 4;
                    }
                } else {
                    state = -1;
                }
                break;

            case 5: // stato finale del T3, , nomi l_z
                if (ch >= 48 && ch <= 57) {
                    if (ch % 2 != 0) {
                        state = 5;
                    } else {
                        state = 6;
                    }
                } else {
                    state = -1;
                }
                break;

            case 6: // stato non finale del T3, nomi l_z
                if (ch >= 48 && ch <= 57) {
                    if (ch % 2 != 0) {
                        state = 5;
                    } else {
                        state = 6;
                    }
                } else {
                    state = -1;
                }
                break;
            }
            i = i + 1;
        }
        return (state == 4 || state == 5);
    }

    public static void main(String[] args) {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}