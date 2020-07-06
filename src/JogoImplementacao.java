import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;


public class JogoImplementacao extends UnicastRemoteObject implements Jogo{

    private final int TAMANHO_TABELA = 10;
    private Jogo jogoAdversario;
    private int id;
    private Stage theStage;
    private ControllerJogoGrid gridController;
    private boolean solicitante;
    private boolean vez;
    private boolean acabou = false;
    private boolean tabelaNavios[][];
    private boolean tabelaNaviosAdversario[][];
    private boolean tabelaRegistroDisparos[][];
    private final Integer navios[][] = {
            {1,5}, //porta-aviões
            {2,4}, //destroyer
            {2,3}, //fragata
            {3,2}, //corvetas
            {3,1}  //submarinos
            
    };
    private int nCasasRestantes;
    private int nCasasRestantesAdversario;


    public JogoImplementacao(Stage stage, boolean solicitante, int id, Jogo jogoAdversario) throws RemoteException{
        this.theStage = stage;
        this.solicitante = solicitante;
        this.vez = solicitante;
        this.jogoAdversario = jogoAdversario;

        this.nCasasRestantes = this.getNCasasRestantes();
        this.nCasasRestantesAdversario = this.getNCasasRestantes();

        this.tabelaNaviosAdversario = new boolean[this.TAMANHO_TABELA][this.TAMANHO_TABELA];
        this.tabelaRegistroDisparos = new boolean[this.TAMANHO_TABELA][this.TAMANHO_TABELA];

        this.id = id;
    }

    public int getNCasasRestantes(){
        int nCasasRestantes = 0;
        for (Integer navio[]: this.navios)
            nCasasRestantes += navio[0] * navio[1];

        return nCasasRestantes;
    }


    public boolean[][] geraTabelaNavios(int tamanho){
        int nTiposNavios = 5;
        ArrayList<Integer[]> listaNavios = new ArrayList<>();

        boolean tabelaNavios[][] = new boolean[tamanho][tamanho];


        for (int i = 0; i < nTiposNavios; ++i)
            listaNavios.add(this.navios[i]);

        Random gen = new Random();

        int nIteracoesLaco = 0;
        while(nTiposNavios > 0) {
            boolean encontrouEspaco = false,horizontal = true;
            int iEscolhido = gen.nextInt(nTiposNavios),
                    x = 0,y = 0,
                    qtdNavio = listaNavios.get(iEscolhido)[0],
                    tamanhoNavio = listaNavios.get(iEscolhido)[1];

            while(!encontrouEspaco){
            if (++nIteracoesLaco >= 700) {
                    System.out.println("Atingiu numero maximo de iterações!");
                    System.out.println(nIteracoesLaco);

                    return null;
                }
                horizontal = gen.nextBoolean();

                if (horizontal){
                    x = gen.nextInt(tamanho - tamanhoNavio);
                    y = gen.nextInt(tamanho);
                }
                else{
                    x = gen.nextInt(tamanho);
                    y = gen.nextInt(tamanho - tamanhoNavio);
                }


                if (x != 0)
                    if (tabelaNavios[x - 1][y])
                        continue;
                if (x + tamanhoNavio + 1 < tamanho)
                    if (tabelaNavios[x + tamanhoNavio + 1][y])
                        continue;

                if (y != 0)
                    if (tabelaNavios[x][y - 1])
                        continue;
                if (y + tamanhoNavio + 1 < tamanho)
                    if (tabelaNavios[x][y + tamanhoNavio + 1])
                        continue;


                boolean encontrouNavio = false;

                for (int i = 0;i < tamanhoNavio;++i) {
                    if (horizontal) {
                        boolean encontrouMesmaPosicao = tabelaNavios[x + i][y],
                                encontrouEsquerda = x > 0 && tabelaNavios[x + i - 1][y],
                                encontrouDireita = x < tamanho - 1 && tabelaNavios[x + i + 1][y],
                                encontrouEmCima = y < tamanho - 1 && tabelaNavios[x + i][y + 1],
                                encontrouEmBaixo = y > 0 && tabelaNavios[x + i][y - 1];
                        if (encontrouMesmaPosicao || encontrouEmCima || encontrouEmBaixo || encontrouEsquerda || encontrouDireita) {
                            encontrouNavio = true;
                            break;
                        }
                    }
                    else {
                        boolean encontrouMesmaPosicao = tabelaNavios[x][y + i],
                                encontrouEsquerda = x > 0 && tabelaNavios[x - 1][y + i],
                                encontrouDireita = x < tamanho - 1 && tabelaNavios[x + 1][y + i],
                                encontrouEmBaixo = y > 0 && tabelaNavios[x][y + i - 1],
                                encontrouEmCima = y < tamanho - 1 && tabelaNavios[x][y + i + 1];
                        if (encontrouMesmaPosicao || encontrouEsquerda || encontrouDireita || encontrouEmCima || encontrouEmBaixo) {
                            encontrouNavio = true;
                            break;
                        }
                    }
                }

                if (encontrouNavio) continue;

                encontrouEspaco = true;
            }

            listaNavios.get(iEscolhido)[0] = --qtdNavio;

            for (int i = 0;i < tamanhoNavio;++i) {
                if (horizontal){
                    tabelaNavios[x + i][y] = true;
                }
                else{
                    tabelaNavios[x][y + i] = true;
                }
            }

            if (qtdNavio == 0) {
                listaNavios.remove(iEscolhido);
                --nTiposNavios;
            }

        }

        return tabelaNavios;
    }



    public void carregarInicioJogo(){

        System.out.println("JogoImpl iniciado!");


        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                System.out.println("A gerar tabela dos navios...");
                while(tabelaNavios == null) {
                    System.out.println("A gerar tabela dos navios...");

                    tabelaNavios = geraTabelaNavios(10);
                }


                try {
                    boolean tabelaNaviosProprio[][] = getTabelaNaviosProprio(),
                            tabelaNaviosAdversario[][] = getTabelaNaviosAdversario();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("TelaPrincipalGrid.fxml"));
                    gridController = new ControllerJogoGrid();

                    loader.setController(gridController);

                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    theStage.setScene(new Scene(root));
                    theStage.show();

                    Platform.runLater(() -> {
                        System.out.println("CARREGOU NOVO INICIO DE JOGO!!!");

                        gridController.setgridAdversario(tabelaNaviosAdversario);
                        gridController.setGridProprio(tabelaNaviosProprio);


                        String textoLabel;
                        if (vez)
                            textoLabel = "É a tua vez!";
                        else
                            textoLabel = "Vez do Adversario!";

                        gridController.mudaTextoLabel(textoLabel);


                        iniciarJogo();
                    });


                }
                catch(RemoteException e){
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    public boolean disparo(int x, int y) throws RemoteException{

        boolean acertou = this.verificaTiro(x, y);
        Color corParaPintar;

        if (acertou){
            corParaPintar = Color.RED;

            if (--this.nCasasRestantes == 0){
                //Acabou o jogo!
                this.acabou = true;
                this.setTextoLabel("Jogo Finalizado,Perdeste!");
            }
        }
        else{
            corParaPintar = Color.BLUE;
            this.trocaVez();

            this.setTextoLabel("É a tua vez!");
        }

        //pinta no tabuleiro do Adversario o resultado do tiro
        this.pintaQuadrado(x,y,corParaPintar.getRed(),corParaPintar.getRed(),corParaPintar.getBlue());


        return acertou;
    }

    public boolean verificaTiro(int x,int y){
        return this.tabelaNavios[x][y];
    }


    public void iniciarJogo(){
        Scene cenaJogo = this.theStage.getScene();
        GridPane gridAdversario = this.gridController.getgridAdversario(),
                 gridProprio = this.gridController.getGridProprio();

        gridAdversario.setOnMouseClicked(event -> {
            if (!vez) return;

            Rectangle rect = (Rectangle)event.getTarget();

            byte x = gridAdversario.getColumnIndex(rect).byteValue(),
                 y = gridAdversario.getRowIndex(rect).byteValue();


            try {

                //verifica se disparo nessas coordenadas já foi realizado
                if (this.tabelaRegistroDisparos[x][y])
                    return;

                boolean acertou = this.jogoAdversario.disparo(x,y);
                Color corParaPintar;

                //regista o disparo
                this.tabelaRegistroDisparos[x][y] = true;

                if (acertou) {
                    corParaPintar = Color.RED;


                    if (--this.nCasasRestantesAdversario == 0) {
                        //Acabou o jogo!
                        this.acabou = true;
                        this.setTextoLabel("Jogo Finalizado,Ganhaste!");
                        jogoAdversario.setTextoLabel("Jogo Finalizado,Perdeste!");
                    }
                }
                else{
                    corParaPintar = Color.BLUE;
                    this.trocaVez();

                    this.setTextoLabel("Vez do Adversario!");
                    jogoAdversario.setTextoLabel("É a tua vez!");
                }
                this.gridController.pintaQuadradoAdversario(x,y,corParaPintar);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        });

    }

    @Override
    public void trocaVez() throws RemoteException {
        this.vez = !this.vez;
    }

    @Override
    public void pintaQuadrado(int x, int y, double r, double g, double b) throws RemoteException {
        this.gridController.pintaQuadradoProprio(x,y,new Color(r,g,b,1));
    }

    public void setFinalizado() throws RemoteException{
        this.acabou = true;
    }

    @Override
    public void setJogoAdversario(Jogo jogoAdversario) throws RemoteException{
        this.jogoAdversario = jogoAdversario;
    }

    @Override
    public void setTextoLabel(String texto) throws RemoteException {
        Platform.runLater(() -> this.gridController.mudaTextoLabel(texto));
    }

    @Override
    public int getID() throws RemoteException {
        return this.id;
    }

    public boolean[][] getTabelaNaviosProprio() throws RemoteException{
        return this.tabelaNavios;
    }


    public boolean[][] getTabelaNaviosAdversario() throws RemoteException{
        return this.tabelaNaviosAdversario;
    }
}
