package org.xel;

import static org.xel.Constants.MAX_SOURCE_SIZE;

public class CodeGetter {
    public static String convert(String elasticPL) throws Exceptions.SyntaxErrorException {
        if(elasticPL.length()>MAX_SOURCE_SIZE) throw new Exceptions.SyntaxErrorException("Code length exceeded");
        TokenManager t = new TokenManager();
        t.build_token_list(elasticPL);
        //t.dump_token_list();
        ASTBuilder.parse_token_list(t.state);
        int wcet = WCETCalculator.calc_wcet(t.state);
        int verify_wcet = WCETCalculator.get_verify_wcet(t.state);
        //System.out.println("Debug: WCETS " + wcet + ", verify " + verify_wcet);
        if(wcet > Constants.ABSOLUTELY_MAXIMUM_WCET){
            throw new Exceptions.SyntaxErrorException("Absolutely maximum WCET of " + Constants
                    .ABSOLUTELY_MAXIMUM_WCET + " exceeded: your script has a WCET of " + wcet + ".");
        }
        if(verify_wcet < 0){
            throw new Exceptions.SyntaxErrorException("Absolutely maximum verify function WCET has a strange value.");
        }
        if(verify_wcet > Constants.ABSOLUTELY_MAXIMUM_VERIFY_WCET){
            throw new Exceptions.SyntaxErrorException("Absolutely maximum verify function WCET of " + Constants
                    .ABSOLUTELY_MAXIMUM_VERIFY_WCET + " exceeded: your script has a verify function WCET of " + wcet + ".");
        }

        CodeConverter.convert_verify(t.state);

        String c_code = "";
        for(String x : t.state.stack_code){
            c_code += x ;
        }

        return c_code;
    }
}
