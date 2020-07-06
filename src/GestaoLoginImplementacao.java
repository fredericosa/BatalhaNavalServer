import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GestaoLoginImplementacao extends UnicastRemoteObject implements GestaoLogin {
    private int counter = 0;
    private boolean JogadorLogado1,JogadorLogado2;

    protected GestaoLoginImplementacao() throws RemoteException {
        super();

        this.JogadorLogado1 = false;
        this.JogadorLogado2 = false;
    }


    @Override
    public boolean registoFinalizado() throws RemoteException{
        return this.counter == 2;
    }

    @Override
    public boolean jogadorPronto(int id) throws RemoteException{
        if (id == 1)
            return this.JogadorLogado1;
        else if (id == 2)return this.JogadorLogado2;

        return this.JogadorLogado1 && this.JogadorLogado2;
    }


    @Override
    public void setJogadorPronto(int id) throws RemoteException{
        if (id == 1)
            this.JogadorLogado1 = true;
        else if(id == 2)
            this.JogadorLogado2 = true;
    }

    @Override
    public int regista() throws RemoteException {
        if (counter >= 2) return 0;

        return ++counter;
    }

    @Override
    public void finaliza(int idJogador) throws RemoteException {
        --counter;

        if (idJogador == 1)
            JogadorLogado1 = false;
        else
            JogadorLogado2 = false;
    }
}
