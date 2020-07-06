import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;


public class ControllerJogoGrid implements Initializable{

    public GridPane gridAdversario;
    public GridPane gridProprio;
    public Label estadoJogo;
    private Rectangle rects[][];
    private Rectangle rectsAdversario[][];
    private Rectangle rectsProprio[][];
    private boolean tabela[][];
    private GridPane gridPane;
    private int tamQuadrado;
    private final int largura = 10;
    private final int altura = 10;
    private final int tamQuadradoAdversario = 50;
    private final int tamQuadradoProprio = 10;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("ABRIU!");
    }

    public void GridController(){

    }

    public void setgridAdversario(boolean[][] tabela){
        gridAdversario.setHgap(this.tamQuadradoAdversario / 10);
        gridAdversario.setVgap(this.tamQuadradoAdversario / 10);

        this.tabela = tabela;
        this.rectsAdversario = new Rectangle[altura][largura];

        for (int x = 0;x < altura;++x){
            for (int y = 0;y < largura;++y) {

                Rectangle rect = new Rectangle(this.tamQuadradoAdversario,this.tamQuadradoAdversario, Color.WHITE);

                gridAdversario.add(rect,x,y);

                this.rectsAdversario[x][y] = rect;
            }
        }

        this.pintagridAdversario();
    }

    public void setGridProprio(boolean[][] tabela){
        gridProprio.setHgap(this.tamQuadradoProprio / 10);
        gridProprio.setVgap(this.tamQuadradoProprio / 10);

        this.tabela = tabela;
        this.rectsProprio = new Rectangle[altura][largura];

        for (int x = 0;x < altura;++x){
            for (int y = 0;y < largura;++y) {

                Rectangle rect = new Rectangle(this.tamQuadradoProprio,this.tamQuadradoProprio, Color.WHITE);

                gridProprio.add(rect,x,y);

                this.rectsProprio[x][y] = rect;
            }
        }

        this.pintaGridProprio();
    }

    private void pintagridAdversario(){
        //pinta todos os navios
        for (int x = 0;x < this.altura;++x)
            for (int y = 0;y < this.largura;++y)
                if (this.tabela[x][y])
                    this.rectsAdversario[x][y].setFill(Color.BLACK);

    }

    private void pintaGridProprio(){
        //pinta todos os navios
        for (int x = 0;x < this.altura;++x)
            for (int y = 0;y < this.largura;++y)
                if (this.tabela[x][y])
                    this.rectsProprio[x][y].setFill(Color.BLACK);

    }

    public void mudaTextoLabel(String texto){
            System.out.println("TEXTO ATUAL LABEL: " + estadoJogo.getText());
            estadoJogo.setText(texto);
    }

    public void pintaQuadradoAdversario(int x, int y, Color cor){
        Platform.runLater(() -> this.rectsAdversario[x][y].setFill(cor));
    }

    public void pintaQuadradoProprio(int x, int y, Color cor){
        Platform.runLater(() -> this.rectsProprio[x][y].setFill(cor));
    }

    public GridPane getgridAdversario(){
        return this.gridAdversario;
    }

    public GridPane getGridProprio(){
        return this.gridProprio;
    }
}


