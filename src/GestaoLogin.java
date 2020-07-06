import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GestaoLogin extends Remote {

    boolean registoFinalizado() throws RemoteException;

    boolean jogadorPronto(int id) throws RemoteException;

    void setJogadorPronto(int id) throws RemoteException;

    int regista() throws RemoteException;
    void finaliza(int idCliente) throws RemoteException;

}
