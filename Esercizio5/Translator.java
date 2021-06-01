import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() { 
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) { 
        throw new Error("near line " + lex.line + ":" +s );
    }

    void match(int t) {
        if(look.tag == t){
            if(look.tag != Tag.EOF){
                move();
            }
        }else error("Syntax error");
    }

    public void prog() {  // Prog :=  EOF  
        if (look.tag == '='|| look.tag == Tag.PRINT || look.tag == Tag.READ ||
         look.tag == Tag.COND || look.tag == Tag.WHILE || look.tag == '{' ){
            int lnext_prog = code.newLabel();
            statlist(lnext_prog);
            code.emitLabel(lnext_prog);
            match(Tag.EOF);
            try {
                code.toJasmin(); //output
            }
            catch(java.io.IOException e) {
                System.out.println("IO error\n");
            };
        }else 
            error("ERROR --> PROG");
    
    }
    public void statlist(int lnext_prog){ //statlist := <stat> <statlistp>
        if(look.tag == '='|| look.tag == Tag.PRINT || look.tag == Tag.READ ||
         look.tag == Tag.COND || look.tag == Tag.WHILE || look.tag == '{' ){
             int snext = code.newLabel();
             stat(snext);
             code.emitLabel(snext);
             statlistp(lnext_prog);
        }else 
            error("ERROR --> STATLIST");
    }

    public void statlistp(int lnext_prog){ //statlistp := ; <stat> <statlistp> | eps
        switch(look.tag){

            case ';':
                int snext_statlp = code.newLabel(); 
                move();
                stat(snext_statlp);
                code.emitLabel(snext_statlp);
                statlistp(lnext_prog);
                break;
            

            case Tag.EOF : //eps transizione 
            case '}': 
                break;

            default: 
                error("ERROR --> STATLISTP");
        }
    }

    public void stat(int snext) {
        int l_true, l_false;
        switch(look.tag) {
            case '=':
                match('=');
                int assign_id_addr = st.lookupAddress(((Word)look).lexeme); //il valore deve tornare -1
                if(look.tag == Tag.ID && assign_id_addr ==-1){
                    assign_id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                match(Tag.ID); //utilizzo OpCode così facendo assegno al 
                //simbolo = istruzione assembler istore 
                expr();
                code.emit(OpCode.istore,assign_id_addr);
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist(1);
                match(')');
                break;

            case Tag.WHILE:
                match(Tag.WHILE);
                int begin = code.newLabel();
                match('(');
                code.emitLabel(begin);
                l_true = code.newLabel();
                l_false = snext;
                bexpr(l_true, l_false);
                code.emitLabel(l_true);
                match(')');
                stat(begin);
                code.emit(OpCode.GOto,begin);
                break;

            case '{':
                match('{');  
                int lnext_prog = code.newLabel();
                statlist(lnext_prog); 
            //passo a statlist il valore snext 
                match('}');
                break;

            //non sono convinto dal whenlist, cosa devo passare
            case Tag.COND: //lo utilizzo per come condizionale
                match(Tag.COND);
                l_false = code.newLabel();
                whenlist(l_false);
                code.emit(OpCode.GOto,snext);
                match(Tag.ELSE);
                code.emitLabel(l_false);
                stat(snext);
                break;

            case Tag.READ:
                match(Tag.READ);
                match('(');
                if (look.tag==Tag.ID) {
                    int read_id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (read_id_addr==-1) {
                        read_id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }                    
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore,read_id_addr); 
                    
                }
                else
                    error("Error in grammar (stat) after read( with " + look);
                break;

            default:
                    error("ERROR --> STAT");
        }
     }

    public void whenlist(int l_false){
        if(look.tag == Tag.WHEN){
            whenitem(l_false);
            whenlistp(l_false);
        }else  
            error("ERROR --> WHENLIST");

    }

    public void whenlistp(int l_false){
        switch(look.tag){
            case Tag.WHEN:
                whenitem(l_false);
                whenlistp(l_false);
            case Tag.ELSE: //epsilon transizione
            break;
            default: 
                error("ERRROR --> WHENLIST");
        }
    }

    public void whenitem( int l_false){
        if(look.tag == Tag.WHEN){
            match(Tag.WHEN);
            int whe = code.newLabel();
            match('(');
            bexpr(whe,l_false);               
            match(')');
            code.emitLabel(whe);
            match(Tag.DO);
            stat(whe);
        }else error("ERROR --> WHENITEM");
    }

    public void bexpr(int whe, int l_false){
        if(look.tag == Tag.RELOP){
            switch(((Word)look).lexeme){
                case "==":
                    match(Tag.RELOP);
                    expr();
                    expr();
                    code.emit(OpCode.if_icmpeq,whe);
                    code.emit(OpCode.GOto,l_false);
                    break;
                case "<":
                    match(Tag.RELOP);
                    expr();
                    expr(); 
                    code.emit(OpCode.if_icmplt,whe);
                    code.emit(OpCode.GOto,l_false);
                    break;
                case ">":
                    match(Tag.RELOP);
                    expr();
                    expr();
                    code.emit(OpCode.if_icmpgt,whe);
                    code.emit(OpCode.GOto,l_false);
                    break;
                case "=>":
                    match(Tag.RELOP);
                    expr();
                    expr();
                    code.emit(OpCode.if_icmpge, whe);
                    code.emit(OpCode.GOto,l_false);
                    break;
                case "=<":
                    match(Tag.RELOP);
                    expr();
                    expr();
                    code.emit(OpCode.if_icmple, whe);
                    code.emit(OpCode.GOto, l_false);
                    break;
                case "<>":
                    match(Tag.RELOP);
                    expr();
                    expr();
                    code.emit(OpCode.if_icmpne, whe);
                    code.emit(OpCode.GOto, l_false);
                    break;
                default:
                    error("ERROR --> BEXPR");
            }
        }
    }

    public void expr(){
        switch(look.tag) {
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '+':
                match('+');
                match('(');
                exprlist(2);
                match(')');
                break;
            case '*':
                match('*');
                match('(');
                exprlist(3);
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc,(((NumberTok) look).lexeme));
                match(Tag.NUM);
                break;
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme); 
                if(id_addr ==-1){
                    error("ERRORE in if tagID expr");
                }
                code.emit(OpCode.iload,id_addr);
                match(Tag.ID);
               
                break;

            default:
                error("ERROR --> EXPR");

        }
    }

    public void exprlist(int num){
        if(look.tag == '+' || look.tag == '-' || 
            look.tag == '*' ||look.tag == '/' || 
            look.tag == Tag.NUM || look.tag == Tag.ID){
            expr();
            if(num == 1){
                code.emit(OpCode.invokestatic,1);
            }
            
            exprlistp(num);
            
        }else error("ERROR-->EXPRLIST");
    }
    public void exprlistp(int num){
        switch(look.tag){
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
            if(num == 1){
                code.emit(OpCode.invokestatic,1);
            }else if(num == 2){
                    code.emit(OpCode.iadd);
            }else if(num == 3){
                    code.emit(OpCode.imul);
            }else {
                error("ERROR in exprlistp non valido");
            }
            
            exprlistp(num);

            break;
            case ')':
                break;
            
            default:
                error("ERROR --> EXPRLISTP");
        }
    }

public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Prova.lft";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator tr = new Translator(lex, br);
            tr.prog();
            System.out.println("\nFile Output.j generato!");
            System.out.println(
                    "Digita 'java -jar jasmin.jar Output.j' per il file Output.class e 'java Output' per eseguirlo.\n");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}