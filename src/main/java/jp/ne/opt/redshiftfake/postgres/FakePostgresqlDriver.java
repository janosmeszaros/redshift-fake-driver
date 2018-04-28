package jp.ne.opt.redshiftfake.postgres;

import jp.ne.opt.redshiftfake.FakeConnection;
import jp.ne.opt.redshiftfake.Global;
import jp.ne.opt.redshiftfake.s3.S3ServiceImpl;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class FakePostgresqlDriver extends Driver {
    private static final String urlPrefix = "jdbc:postgresqlredshift";

    static {
        try {
            DriverManager.registerDriver(new FakePostgresqlDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (url.startsWith(urlPrefix)) {
            String postgresUrl = url.replaceFirst(urlPrefix, "jdbc:postgresql");
            return new FakeConnection(
                    DriverManager.getConnection(postgresUrl, info),
                    new S3ServiceImpl(Global.s3Endpoint())
            );
        } else {
            return null;
        }
    }

    @Override
    public boolean acceptsURL(String url) {
        return url.startsWith(urlPrefix);
    }
}
