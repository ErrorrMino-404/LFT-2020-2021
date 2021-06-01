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
        peek = ' ';
        return Token.div;
      

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
        peek = ' ';
        return Word.ge;
     }
      peek = ' ';
      return Word.gt;

    case '<':
      readch(br);
      if(peek == '>'){
        peek = ' ';
        return Word.ne;
      }else if(peek == '=') {
        peek = ' ';
        return Word.le;
      }

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
          if (Character.isLetter(peek)  || peek == '_') {
            String parola;
            for(parola  = ""; Character.isLetter(peek)||peek == '_'; readch(br)){
                parola += peek;
            }
            if (parola.equals("cond")){
              return Word.cond;
            }else if (parola.equals("when")){
              return Word.when;
            }else if (parola.equals("then")){
              return Word.then;
            }else if (parola.equals("else")){
              return Word.elsetok;
            }else if (parola.equals("while")){
              return Word.whiletok;
            }else if (parola.equals("do")){
              return Word.dotok;
            }else if (parola.equals("seq")){
              return Word.seq;
            }else if (parola.equals("print")){
              return Word.print;
            }else if (parola.equals("read")){
              return Word.read;
            }else {return new Word(Tag.ID, parola); }
    
        }else if (Character.isDigit(peek)|| Character.isLetter(peek)) {
              String numero;
              for(numero = ""; Character.isDigit(peek) ||Character.isLetter(peek) ;readch(br)){
                numero = numero + peek;
              }
              if(numero.charAt(0) == '0' && numero.length() > 1){
                System.err.println("valore 0 seugito di altre stringhe  " + numero);
                return null;
              }else for(int i = 0; i < numero.length(); i++){
                      if(!(numero.charAt(i) >= 47 && numero.charAt(i) <= 57)){
                        System.err.println("Numero prima di un identificatore errato   " + numero);
                        return null;
                }
              } return new NumberTok(Tag.NUM, Integer.valueOf(numero));
          } else {
            System.err.println("Erroneous character: " + peek );
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
