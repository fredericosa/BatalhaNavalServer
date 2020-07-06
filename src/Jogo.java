import javafx.scene.paint.Color;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface Jogo extends Remote {

    int getNCasasRestantes() throws RemoteException;

    boolean[][] geraTabelaNavios(int tamanho) throws RemoteException;

    void carregarInicioJogo() throws RemoteException;

    boolean disparo(int x, int y) throws RemoteException;

    boolean verificaTiro(int x,int y) throws RemoteException;

    void iniciarJogo() throws RemoteException ;

    void trocaVez() throws RemoteException;

    void pintaQuadrado(int x,int y,double r, double g, double b) throws RemoteException;

    void setFinalizado() throws RemoteException;

    void setJogoAdversario(Jogo jogoAdversario) throws RemoteException;

    void setTextoLabel(String texto) throws RemoteException;

    int getID() throws RemoteException;

    boolean[][] getTabelaNaviosProprio() throws RemoteException;

    boolean[][] getTabelaNaviosAdversario() throws RemoteException ;
}
