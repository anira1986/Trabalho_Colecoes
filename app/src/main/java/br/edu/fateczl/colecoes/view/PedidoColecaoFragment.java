package br.edu.fateczl.colecoes.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.fateczl.colecoes.R;
import br.edu.fateczl.colecoes.controller.ColecaoPedidoController;
import br.edu.fateczl.colecoes.model.Cliente;
import br.edu.fateczl.colecoes.model.Item;
import br.edu.fateczl.colecoes.model.PedidoColecao;
import br.edu.fateczl.colecoes.persistence.ColecaoPedidoDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PedidoColecaoFragment extends Fragment {
    private View view;
    private EditText editTextPedidoColecaoId;
    private TextView textViewClienteOutput;
    private TextView textViewPedidoColecaoOutput;
    private ColecaoPedidoController colecaoPedidoController;

    public PedidoColecaoFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pedido_colecao, container, false);

        editTextPedidoColecaoId = view.findViewById(R.id.editTextPedidoColecaoId);
        textViewClienteOutput = view.findViewById(R.id.textViewClienteOutput);
        textViewClienteOutput.setMovementMethod(new ScrollingMovementMethod());
        textViewPedidoColecaoOutput = view.findViewById(R.id.textViewPedidoColecaoOutput);
        textViewPedidoColecaoOutput.setMovementMethod(new ScrollingMovementMethod());

        colecaoPedidoController = new ColecaoPedidoController(new ColecaoPedidoDAO(this.getContext()));

        view.findViewById(R.id.buttonPesquisarPedidos).setOnClickListener(searchOrders -> searchOrders());
        view.findViewById(R.id.buttonPesquisarPedidoId).setOnClickListener(searchPedido -> searchPedido());
        view.findViewById(R.id.buttonRemoverPedido).setOnClickListener(removePedido -> removePedido());

        return view;
    }

    private void searchOrders() {
        try {
            List<PedidoColecao> pedidos = colecaoPedidoController.listEntry();

            StringBuilder stringBuffer = new StringBuilder();

            for (PedidoColecao pedido : pedidos) {
                stringBuffer.append(pedido.toString()).append("\n");
            }

            textViewClienteOutput.setText(stringBuffer.toString());

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void searchPedido() {
        PedidoColecao pedido;

        try {
            if (editTextPedidoColecaoId.length() == 0) {
                throw new IllegalArgumentException("Entrada inválida");
            }

            pedido = colecaoPedidoController.searchEntry(new PedidoColecao(
                    Integer.parseInt(editTextPedidoColecaoId.getText().toString()),
                    null, LocalDate.now()));

            if (pedido != null) {
                StringBuilder stringBuffer = new StringBuilder();

                stringBuffer.append(String.format("Pedido: %s\n", String.valueOf(pedido.getId())));
                stringBuffer.append(String.format("Cliente: %s\n", pedido.getCliente().getNome()));
                stringBuffer.append(String.format("Data: %s\n", pedido.getDataPedido().toString()));
                stringBuffer.append(String.format("Entrega Estimada: %s\n", pedido.getDataEntrega().toString()));

                List<Item> itens = pedido.getItens();
                for (Item item : itens) {
                    stringBuffer.append(item.toString()).append("\n");
                }

                textViewPedidoColecaoOutput.setText(stringBuffer.toString());

            } else {
                Toast.makeText(view.getContext(), "Pedido não encontrado", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void removePedido() {
        try {
            if (editTextPedidoColecaoId.length() == 0) {
                throw new IllegalArgumentException("Entrada inválida");
            }

            colecaoPedidoController.removeEntry(new PedidoColecao(
                    Integer.parseInt(editTextPedidoColecaoId.getText().toString()),
                    null, LocalDate.now()));

            Toast.makeText(view.getContext(), "Pedido removido com sucesso", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearPedidoValues();
    }

    private void clearPedidoValues() {
        editTextPedidoColecaoId.setText("");
    }
}
