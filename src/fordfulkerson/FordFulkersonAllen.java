package fordfulkerson;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * A classe AuxGrafo foi implementada principalmente para mostrar todos os
 * caminhos possíveis de uma vértice a outra através de suas ligações.
 *
 * @author Allen Hichard Marques dos Santos, Alisson Vilas Verde
 */
public class FordFulkersonAllen {

    private final Map<Vertice, LinkedHashSet<Aresta>> mapa = new HashMap();
    private LinkedList<Vertice> visitado;
    private Vertice start;
    private Vertice ultimo;
    private List distancias = new LinkedList();
    private List<List> lista = new LinkedList();
    private double distancia;
    double menor = Double.MAX_VALUE;
    private LinkedList menoresArestas = new LinkedList<>();
    private LinkedList Arestas = new LinkedList<>();
    private double fluxomaximo = 0;
    
    
    
    /**
     * O método adicionar, insere em uma tabela hash os dois nomes que nesse
     * caso é considerado duas vertices. Só um detalhe a classe tem um
     * comportamento igual a um grafo, mas com finalidade apenas de mostrar
     * todos os caminhos possíveis.
     *
     * @param nome1 primeira vertice
     * @param nome2 segunda vertice
     */
    public void adicionar(Vertice nome1, Vertice nome2, double peso) {
        LinkedHashSet<Aresta> adjacente = mapa.get(nome1); // C1
        if (adjacente == null) { // 1
            adjacente = new LinkedHashSet(); // C2
            mapa.put(nome1, adjacente); //C3
        }
        Aresta a = new Aresta(nome2, peso); //C4
        adjacente.add(a); //C6
        //Complexidade do método adicionar é apenas uma constante
        // C = (C1 + C2 + C3 + C4 + C5) 
        // O(C + 1) = T1

    }

    /**
     * Liga as duas vértices em virse-versa. Por exemplo A liga B e B liga A.
     *
     * @param nome1
     * @param nome2
     */
    public void adicionarDuplo(Vertice nome1, Vertice nome2, double peso) {
        adicionar(nome1, nome2, peso); // tempo C do adicionar 
        //adicionar(nome2, nome1, peso); // tempo C do adicionar
        // A complexidade do adicionar duplo é duas vezes o adicionar
        // O (2*C + 2) = T2
    }

    /**
     * Não permite dois lugares com o mesmo nome, método não utilizado no
     * problema já que pode haver duas cidades com o mesmo nome."Talves possa
     * não ficou nada restrito quando a isso"
     *
     * @param nome1
     * @param nome2
     * @return
     */
    public boolean estaConectado(Vertice nome1, Vertice nome2) {
        Set adjacente = mapa.get(nome1); // C1
        if (adjacente == null) { //1
            return false; // 1
        }
        return adjacente.contains(nome2); //1
        //O(C1 + 2) = T3;
    }

    /**
     * O método retorna uma lista que contem uma hash.
     *
     * @param last ultimo da atual hash.
     * @return
     */
    public LinkedList<Aresta> nosAdjacente(Vertice last) {
        LinkedHashSet<Aresta> adjacente = mapa.get(last); //C1 + tempo da Hash(provavel O(1))
        if (adjacente == null) { // 1
            return new LinkedList(); // 1
        }
        return new LinkedList(adjacente); //1
        // 0(C1 + 2) + provavel O(1) = T4
    }

    /**
     * Método que permite a busca de todos caminhos possíveis do um ponto a
     * outro. Por ser recursivo ele pega todos os caminhos e vao salvando em uma
     * lista no método printarNaLista, que guarda cada caminho em uma posição.
     *
     * @param ultimo o ponto dois.
     * @return a listas com todos os caminhos.
     */
    public List todosCaminhos(Vertice ultimo) {
        
        LinkedList<Aresta> nodes = nosAdjacente(visitado.getLast()); // C1 + T4
        
        //O algoritmo tem comportamento de uma matriz, fazendo combinação entre todos os vertices do grafo
        //Este laço executa A*Quantidade de Recurvisade que pe dado pena quantidade de vertice
        //Para maioria dos Casos O|A*V|
        for (Aresta node : nodes) {  
            if (visitado.contains(node.getDestino())) {
                continue;
            }
            if (node.equals(ultimo)) {
                menoresArestas.add(node.getPeso());
                Arestas.add(node);
                System.out.println("Normal");
                System.out.println(menoresArestas.toString());
                distancia += node.getPeso();
                visitado.add(node.getDestino());
                distancias.add(distancia);
                printarNaLista(visitado);
                visitado.removeLast();
                Arestas.removeLast();
                menoresArestas.removeLast();
                distancia -= node.getPeso();
                break;
            }
        }
        for (Aresta node : nodes) {  //O mesmo vale para este laço O|A*V|
            if (visitado.contains(node) || node.equals(ultimo)) {
                continue;
            }
            menoresArestas.add(node.getPeso());
            Arestas.add(node);
            distancia += node.getPeso();
            visitado.addLast(node.getDestino());
            todosCaminhos(ultimo);
            visitado.removeLast();
            menoresArestas.removeLast();
            Arestas.removeLast();
            distancia -= node.getPeso();
        }
        return lista;

        // tempo Total 2*A*V
    }

    public void limpar() {
        lista = new LinkedList<>();
    }

    /**
     * O método concatena caminho por caminho salvando em uma lista, para depois
     * ser retornado pelo método primeiraBorda.
     *
     * @param visitado listas de visitados do caminho atual.
     */
    private LinkedList printarNaLista(LinkedList<Vertice> visitado) {
        List<Vertice> nova = new LinkedList();
        for (Vertice node : visitado) { // T6 = O(V + 1)
            nova.add(node);
        }
        lista.add(nova); //C
        List s = new LinkedList(menoresArestas);
        Collections.sort(s);
        menoresArestas = new LinkedList();
        System.out.println("Ordenada");
        System.out.println(s.toString());
        double menor = (double)s.get(0);
        fluxomaximo +=menor;
        Iterator it = Arestas.iterator();
        while(it.hasNext()){
            Aresta a = (Aresta) it.next();
            a.setPeso(a.getPeso()- menor);
            menoresArestas.add(a.getPeso());
            
        }
        return menoresArestas;
        
    }

    /**
     * retorna o ponto inicial.
     *
     * @return
     */
    public Vertice getInicio() {
        return start;
    }

    /**
     * recebe um ponto inicial.
     *
     * @param start inicio
     */
    public void setInicio(Vertice start) {
        visitado = new LinkedList();
        this.start = start;
        visitado.add(start);
    }

    public void Visualizar(List list) {
        Iterator it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            List l = (List) it.next();
            Iterator ut = l.iterator();
            while (ut.hasNext()) {
                Vertice a = (Vertice) ut.next();
                System.out.print(a.toString() + " ");
            }
            System.out.println(distancias.get(i));
            i++;
            System.out.println("");
        }
        System.out.println("Fluxo Máximo");
        System.out.println(fluxomaximo);
        
    }
  

    /**
     * retorna o ponto final.
     *
     * @return
     */
    public Vertice getFinal() {
        return ultimo;
    }

    /**
     * recebe um ponto final.
     *
     * @param ultimo final
     */
    public void setFinal(Vertice ultimo) {
        this.ultimo = ultimo;
    }

    public static void main(String[] args) {
    
        

        FordFulkersonAllen grafo = new FordFulkersonAllen();
        Vertice s = new Vertice("S");
        Vertice a = new Vertice("A");
        Vertice b = new Vertice("B");
        Vertice c = new Vertice("C");
        Vertice d = new Vertice("D");
        Vertice t = new Vertice("T");
       
        
        
        grafo.adicionarDuplo(s, a, 16);
        grafo.adicionarDuplo(s, c, 13);
        grafo.adicionarDuplo(a, b, 12);
        grafo.adicionarDuplo(c, a, 4);
        grafo.adicionarDuplo(b, c, 9);
        grafo.adicionarDuplo(b, t, 20);
        grafo.adicionarDuplo(c, d, 14);
        grafo.adicionarDuplo(d, b, 7);
        grafo.adicionarDuplo(d, t, 4);
        
        //grafo.adicionarDuplo(a, e, 1);
        
        grafo.setInicio(s); // Origem
        List list = grafo.todosCaminhos(t); // Destino
        grafo.Visualizar(list);
       

    }
}
