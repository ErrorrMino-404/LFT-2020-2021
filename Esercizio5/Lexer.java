import java.io.*;
import java.util.*;
public class Lexer {
public static int line = 1;
  private char peek = ' ';
  private void readch(BufferedReader br) {
    try {
      peek = (char) br.read();
    } catch (IOException exc) {
      peek = (char) -1; // ERROR
    }
  }
  public Token lexical_scan(BufferedReader br) {
    while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
      if (peek == '\n') line++;
      readch(br);
    }
    switch (peek) {
      case '!':
        peek = ' ';
      return Token.not;
// ... gestire i casi di   -, *, /, ; ... //

      case '(':
        peek = ' ';
        return Token.lpt;

      case ')':
        peek = ' ';
        return Token.rpt;

      case '{':
        peek = ' ';
        return Token.lpg;

      case '}':
        peek = ' ';
        return Token.rpg;

      case '+':
        peek = ' ';
        return Token.plus;

      case '-':
        peek = ' ';
        return Token.minus;

      case '*':
        peek = ' ';
        return Token.mult;

      case '/':
        readch(br);
         if(peek == '*') {
          boolean close = false;
          while(!close){
            readch(br);
            if((int)peek == 65535){ //65535 è il limite di un commento
              System.err.println("Nessuna chiusura del commento");
              return null;
            }
              else if(peek == '*'){
                readch(br);
                if(peek=='/'){
                  close = true;
                  readch(br);
                  break;
                }else if(peek=='*'){
                  while(peek=='*'){
                    readch(br);
                    if(peek == '/'){
                      close = true;
                      readch(br);
                      break;
                    }
                  }
                }
              }
              
          }
          return lexical_scan(br);
        }else if (peek == '/'){
          while ((int) peek != 65535 && peek != '\n') {
          readch(br);
          }          
          return lexical_scan(br);
      }else {
        peek = ' ';
        return Token.div;
      }

    case ';':
      peek = ' ';
      return Token.semicolon;

    case '=':
      readch(br);
      if(peek == '='){
        peek = ' ';
        return Word.eq;
      }else{
        peek = ' ';
        return Token.assign;
      }
    case '&':
        readch(br);
        if (peek == '&') {
          peek = ' ';
          return Word.and;
        } else {
          System.err.println("Erroneous character" + " after & : " + peek );
          return null;
        }
          // ... gestire i casi di ||,  <=, >=, ==, <>, = ... //
    case '>':
      readch(br);
      if(peek == '='){
      
        return Word.ge;
     }
      
      return Word.gt;

    case '<':
      readch(br);
      if(peek == '>'){
        
        return Word.ne;
      }else if(peek == '=') {
      
        return Word.le;
      } 
        return Word.lt;  //<---- ERRORR STUPIDO

      case '|':
        readch(br);
        if(peek == '|'){
          peek = ' ';
          return Word.or;
        }else {
          System.err.println("Erroneous character" + " after | : " + peek );
          return null;
        }


      case (char)-1:
          return new Token(Tag.EOF);

      default:
      if (Character.isLetter(peek) || peek == '_') { 
        String parola;
        for (parola = ""; Character.isLetter(peek) || Character.isDigit(peek) || peek == '_'; readch(br)) {
            parola += peek;
        }
        if (parola.equals("when")) {
          return Word.when;
        } else if (parola.equals("cond")) {
          return Word.cond;
        } else if (parola.equals("then")) {
          return Word.then;
        } else if (parola.equals("else")) {
          return Word.elsetok;
        } else if (parola.equals("while")) {
          return Word.whiletok;
        } else if (parola.equals("do")) {
            return Word.dotok;
        } else if (parola.equals("seq")) {
            return Word.seq;
        } else if (parola.equals("print")) {
            return Word.print;
        } else if (parola.equals("read")) {
            return Word.read;
        } else {
        if (parola.charAt(0) == '_') {
          for (int i = 0; i < parola.length(); i++) {
            if (parola.charAt(i) != '_') {
              return new Word(Tag.ID, parola);
            }
        }
            System.err.println("Not a valid identifier, underscore must be followed by letters/digits:\t"+ parola);
            return null;
            } else {
              return new Word(Tag.ID, parola);
          }
        }
      } else if (Character.isDigit(peek)) { 
        String numero;
      for (numero = ""; Character.isDigit(peek) || Character.isLetter(peek); readch(br)) {
        numero += peek;
      }
      if (numero.charAt(0) == '0' && numero.length() > 1) { 
        System.err.println("Not a valid number, 0 followed by character or digit:\t" + numero);
        return null; 
      } else {
        for (int i = 0; i < numero.length(); i++) {
          if (!(numero.charAt(i) >= 48 && numero.charAt(i) <= 57)) {
            System.err.println("Not a valid number (digits only):\t" + numero);
            return null;
          }
        }
      return new NumberTok(Tag.NUM, Integer.valueOf(numero));
      }
    } else {
      System.err.println("Erroneous character: " + peek);
      return null;
    }
  }
}
    public static void main(String[] args) {
      Lexer lex = new Lexer();
      String path = "prova.txt"; // il percorso del file da leggere
      try {
        BufferedReader br = new BufferedReader(new FileReader(path));
        Token tok;
        do {
          tok = lex.lexical_scan(br);
          System.out.println("Scan: " + tok);
        } while (tok.tag != Tag.EOF);
          br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
  }
