package notifi.serialization;

public class NoTiFiValidator {



    public static void validUnsignedInteger(int userId) {

        if(userId < 0){
            throw new IllegalArgumentException("User ID is too large");
        }

    }




    public static void validString(String param){
        if(param == null){
            throw new IllegalArgumentException("Error Message cannot be null");
        }

        if(param.length() > 65505){
            throw new IllegalArgumentException("Error Message Does Not Fit");
        }

        for(int i = 0; i < param.length(); i++){
            int val = param.charAt(i);

            if(val < 32 || val > 126){
                throw new IllegalArgumentException("Error Message must only contain ASCII-printable characters: "+ param);
            }
        }
    }








}
