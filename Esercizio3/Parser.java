import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
	throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
	if (look.tag == t) { //look.tag legge i simboli, quindi devo ciclare questo
	    if (look.tag != Tag.EOF)
          move();
	} else error("syntax error");
    }

    public void start() {
	     if (look.tag == '(' || look.tag == Tag.NUM){
        	expr();
        	match(Tag.EOF);
      }else {
        error("ERROR --> START");
      }
  }

    private void expr() {
      if(look.tag == '(' || look.tag == Tag.NUM){
        term();
        exprp();
      }else {
        error("ERROR --> EXPR");
      }
    }

    private void exprp() {
	     switch (look.tag) {
	        case '+':
            match('+');
            term();
            exprp();
            break;

          case '-':
            match('-');
            term();
            exprp();
            break;

          case ')': //eps transazioni
          case Tag.EOF :
            break;

          default:
            error("Error in EXPRP");
	     }
    }

    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM){
          fact();
          termp();
        }else {
          error("Error TERM");
        }
    }

    private void termp() {
        switch(look.tag){
          case '*':
            match('*');
            fact();
            termp();
            break;
          case '/':
            match('/');
            fact();
            termp();
            break;

            case'+':
            case'-':
            case')'://eps caso
            case Tag.EOF:
              break;

            default:
              error("ERROR --> TERMP");
        }
    }

    private void fact() {
      switch(look.tag){
        case '(':
          match('(');
          expr();
          match(')');
          break;

        case Tag.NUM:
          match(Tag.NUM);
          break;

        default:
          error("ERROR --> FACT");
      }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "doc1.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
