package Etapas;

public class ArbolSintactico {
    private final Nodo root;

    public ArbolSintactico(Nodo root) {
        this.root = root;
    }

    public String PreOrder(Nodo root) {

        if (root == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(root.getNombre());

        String pointerRight = "'-->";
        String pointerLeft = (root.getDer() != null) ? "|-->" : "'-->";

        travelTree(sb, "", pointerLeft, root.getIzq(), root.getDer() != null);
        travelTree(sb, "", pointerRight, root.getDer(), false);

        return sb.toString();
    }

    // Método que muestra la forma arborescente y el contenido de los nodos
    public void travelTree(StringBuilder sb, String padding, String pointer, Nodo node, boolean hasRightBrother) {

        if (node != null) {
            sb.append("\n");
            sb.append(padding);             // Relleno
            sb.append(pointer);             // Apunta a izquierda o derecha
            sb.append(node.getNombre());    // Muestra el nombre
            if (!node.getTipo().equals("SinTipo"))
                sb.append(" ").append(node.getTipo());

            StringBuilder paddingBuilder = new StringBuilder(padding);

            if (hasRightBrother) {
                paddingBuilder.append("|  ");   // Agrego una rama
            } else {
                paddingBuilder.append("   ");
            }

            String paddingBoth = paddingBuilder.toString();
            String pointerRight = "'-->";
            String pointerLeft = (node.getDer() != null) ? "|-->" : "'-->";

            travelTree(sb, paddingBoth, pointerLeft, node.getIzq(), node.getDer() != null);
            travelTree(sb, paddingBoth, pointerRight, node.getDer(), false);
        }
    }
    public void print(OutputManager os) {
        os.write(PreOrder(this.root));
    }

}
