package haur_ranking.repository.winmss_repository;

import java.io.File;
import java.io.IOException;

import com.healthmarketscience.jackcess.CryptCodecProvider;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

import net.ucanaccess.jdbc.JackcessOpenerInterface;

// Used for opening the WinMSS database (MS Access Database)
public class CryptCodecOpener implements JackcessOpenerInterface {
	@Override
	public Database open(File fl, String pwd) throws IOException {
		DatabaseBuilder dbd = new DatabaseBuilder(fl);
		dbd.setAutoSync(false);
		dbd.setCodecProvider(new CryptCodecProvider(pwd));
		dbd.setReadOnly(false);
		return dbd.open();
	}
}
