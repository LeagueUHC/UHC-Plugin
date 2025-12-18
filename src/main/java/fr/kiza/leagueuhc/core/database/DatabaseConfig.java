package fr.kiza.leagueuhc.core.database;

public final class DatabaseConfig {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public DatabaseConfig(String host,int port,String database,String username,String password){
        this.host=host;
        this.port=port;
        this.database=database;
        this.username=username;
        this.password=password;
    }

    public String getJdbcUrl(){
        return "jdbc:mysql://"+host+":"+port+"/"+database+
                "?useSSL=false&useUnicode=true&characterEncoding=utf8";
    }

    public String getUsername(){return username;}

    public String getPassword(){return password;}
}
