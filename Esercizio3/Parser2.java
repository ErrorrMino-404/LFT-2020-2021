import java.io.*;

public class Parser2 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser2(Lexer l, BufferedReader br) {
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

    public void prog() {
      if(look.tag == '=' || look.tag == Tag.PRINT || look.tag == Tag.READ ||
         look.tag == Tag.COND || look.tag == Tag.WHILE || look.tag == '{' ){
           statlist();
           match(Tag.EOF);
      }else {
        error("ERROR --> PROG");
      }
  }

  private void statlist(){
    if (look.tag == '=' || look.tag == Tag.PRINT || look.tag == Tag.READ ||
       look.tag == Tag.COND || look.tag == Tag.WHILE || look.tag == '{' ) {
        stat();
        statlistp();
    } else {
        error("ERROR --> STATLIST");
    }
  }

  private void statlistp(){
    switch(look.tag){
      case ';':
        move();
        stat();
        statlistp();
        break;

      case Tag.EOF: //produzione epsilon
      case '}':
        break;

      default:
        error("ERROR --> STATLISTP");
    }
  }

  private void stat(){
    switch(look.tag){

        case '=':
          match('=');
          match(Tag.ID);
          expr();
          break;

        case Tag.PRINT:
          match(Tag.PRINT);
          match('(');
          exprlist();
          match(')');
          break;

        case Tag.READ:
          match(Tag.READ);
          match('(');
          match(Tag.ID);
          match(')');
          break;

        case Tag.COND:
          match(Tag.COND);
          whenlist();
          match(Tag.ELSE);
          stat();
          break;

        case Tag.WHILE:
          match(Tag.WHILE);
          match('(');
          bexpr();
          match(')');
          stat();
          break;

        case '{':
          match('{');
          statlist();
          match('}');
          break;
    }

  }
  private void whenlist(){
    if(look.tag == Tag.WHEN){
      whenitem();
      whenlistp();
    }else {
      error("ERROR --> WHENLIST");
    }
  }

    private void whenlistp() {
      switch(look.tag){
        case Tag.WHEN:
          whenitem();
          whenlistp();
          break;

        case Tag.ELSE: //epsilon transizione
          break;
        default:
          error("ERROR --> WHENLISTP");
      }
    }

    private void whenitem() {
      if(look.tag == Tag.WHEN){
        match(Tag.WHEN);
        match('(');
        bexpr();
        match(')');
        match(Tag.DO);
        stat();
      }else{
        error("ERROR --> WHENITEM");
      }
    }

    private void bexpr() {
      if(look.tag == Tag.RELOP){
          match(Tag.RELOP);
          expr();
          expr();
      }else {
        error("ERROR --> BEXPR");
      }
    }

    private void expr() {
      switch(look.tag){
        case '+':
          match('+');
          match('(');
          exprlist();
          match(')');
          break;

        case '-':
          match('-');
          expr();
          expr();
          break;

        case '*':
          match('*');
          match('(');
          exprlist();
          match(')');
          break;

        case '/':
          match('/');
          expr();
          expr();
          break;

        case Tag.NUM:
          match(Tag.NUM);
          break;

        case Tag.ID:
          match(Tag.ID);
          break;
        default :
          error("ERROR --> EXPR");
      }
    }

    private void exprlist() {
      if (look.tag == '+' || look.tag == '-' || look.tag == '*' ||
         look.tag == '/' || look.tag == Tag.NUM || look.tag == Tag.ID ){
            expr();
            exprlistp();
        }else {
          error("ERROR --> EXPRLIST");
        }
    }

    private void exprlistp(){
        switch(look.tag){
          case '+':
          case '-':
          case '*':
          case '/':
          case Tag.NUM:
          case Tag.ID:
            expr();
            exprlistp();
            break;

          case ')': //eps produzione
            break;

          default:
            error("ERROR --> EXPRLISTP");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "doc1.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser2 parser = new Parser2(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
