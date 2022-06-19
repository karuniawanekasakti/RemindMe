# Penyesuaian

Sesuaikan IP Address pada RetroServer.java

    public class RetroServer {
	    private static final String baseURL = "http://10.0.2.2/db_databse/";
	    private static Retrofit retro;

	    public static Retrofit konekRetrofit(){
	        if(retro == null){
	            retro = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
	        }
	        
	        return retro;
	    }
    }


