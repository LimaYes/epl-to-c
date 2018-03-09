package org.xel;

import static org.xel.EplToCConstants.MAX_SOURCE_SIZE;

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
        if(wcet > EplToCConstants.ABSOLUTELY_MAXIMUM_WCET){
            throw new Exceptions.SyntaxErrorException("Absolutely maximum WCET of " + EplToCConstants
                    .ABSOLUTELY_MAXIMUM_WCET + " exceeded: your script has a WCET of " + wcet + ".");
        }
        if(verify_wcet < 0){
            throw new Exceptions.SyntaxErrorException("Absolutely maximum verify function WCET has a strange value.");
        }
        if(verify_wcet > EplToCConstants.ABSOLUTELY_MAXIMUM_VERIFY_WCET){
            throw new Exceptions.SyntaxErrorException("Absolutely maximum verify function WCET of " + EplToCConstants
                    .ABSOLUTELY_MAXIMUM_VERIFY_WCET + " exceeded: your script has a verify function WCET of " + wcet + ".");
        }

        CodeConverter.convert_verify(t.state);

        // here, make sure to add global arrays to C file
        String c_code = "";

        c_code += "int i[" + t.state.ast_vm_ints + "];\n";
        c_code += "uint u[" + t.state.ast_vm_uints + "];\n";
        c_code += "double d[" + t.state.ast_vm_doubles + "];\n";
        c_code += "float f[" + t.state.ast_vm_floats + "];\n";
        c_code += "long l[" + t.state.ast_vm_longs + "];\n";
        c_code += "ulong ul[" + t.state.ast_vm_ulongs + "];\n";
        c_code += "uint m[12];\n";
        c_code += "uint s[" + t.state.ast_submit_sz + "];\n";


        for(String x : t.state.stack_code){
            c_code += x ;
        }

        return c_code;
    }
}
