import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        InterfaceUsuario ui = new ConsoleUI(scanner);
        RegraDano regraDano = new CalculadoraDano();
        MotorDeCombate motor = new MotorDeCombate(ui, regraDano);

        ui.mostrar("=== CRÔNICAS DE ELDORIA (RPG POO & SOLID) ===");
        ui.mostrar("Escolha sua classe: 1-Guerreiro | 2-Mago | 3-Arqueiro");
        int op = ui.lerOpcao(1, 3);

        Personagem heroi = (op == 1) ? new Guerreiro("Herói Guerreiro")
                         : (op == 2) ? new Mago("Herói Mago")
                                     : new Arqueiro("Herói Arqueiro");

        heroi.getInventario().adicionarPocao(new PocaoCura("Poção de Cura", 45));
        heroi.getInventario().adicionarPocao(new PocaoCura("Poção Grande", 65));
        heroi.equipar(new Equipamento("Amuleto de Aço", 5, 3), ui);

        List<Inimigo> encontros = List.of(
            new Inimigo("Goblin Batedor", 65, 18, 6, new Equipamento("Espada Rúnica", 8, 2)),
            new Chefao("Dragão Malakor (Chefão)", 125, 25, 10)
        );

        for (Inimigo inimigo : encontros) {
            if (!motor.batalhar(heroi, inimigo)) {
                ui.mostrar("\n[FIM DE JOGO] Você foi derrotado em combate.");
                return;
            }
        }
        ui.mostrar("\n[VITÓRIA FINAL] Parabéns! Você derrotou todos os inimigos e venceu o RPG!");
    }

    public interface InterfaceUsuario {
        void mostrar(String msg);
        int lerOpcao(int min, int max);
    }

    public interface RegraDano {
        int calcular(int ataque, int defesa);
    }

    public interface Combatente {
        String getNome();
        int getVida();
        int getVidaMax();
        int getAtaqueTotal();
        int getDefesaTotal();
        boolean estaVivo();
        int receberDano(int dano);
        void atacar(Combatente alvo, RegraDano regra, InterfaceUsuario ui);
    }

    public interface ItemConsumivel {
        String getNome();
        void usar(Personagem alvo, InterfaceUsuario ui);
    }

    public interface ItemEquipavel {
        String getNome();
        int getBonusAtaque();
        int getBonusDefesa();
    }

    public static class CalculadoraDano implements RegraDano {
        @Override
        public int calcular(int ataque, int defesa) {
            return Math.max(2, ataque - (defesa / 2));
        }
    }

    public static class ConsoleUI implements InterfaceUsuario {
        private final Scanner scanner;

        public ConsoleUI(Scanner scanner) {
            this.scanner = scanner;
        }

        @Override
        public void mostrar(String msg) {
            System.out.println(msg);
        }

        @Override
        public int lerOpcao(int min, int max) {
            while (true) {
                System.out.print("Escolha [" + min + "-" + max + "]: ");
                if (!scanner.hasNextLine()) return min;
                try {
                    int op = Integer.parseInt(scanner.nextLine().trim());
                    if (op >= min && op <= max) return op;
                } catch (NumberFormatException ignored) {}
                System.out.println("Opção inválida!");
            }
        }
    }

    public static class PocaoCura implements ItemConsumivel {
        private final String nome;
        private final int cura;

        public PocaoCura(String nome, int cura) {
            this.nome = nome;
            this.cura = cura;
        }

        @Override
        public String getNome() { return nome + " (+" + cura + " HP)"; }

        @Override
        public void usar(Personagem alvo, InterfaceUsuario ui) {
            int recuperado = alvo.curar(cura);
            ui.mostrar(" [ITEM] " + alvo.getNome() + " usou " + nome + " e recuperou " + recuperado + " HP!");
        }
    }

    public static class Equipamento implements ItemEquipavel {
        private final String nome;
        private final int bonusAtaque;
        private final int bonusDefesa;

        public Equipamento(String nome, int bonusAtaque, int bonusDefesa) {
            this.nome = nome;
            this.bonusAtaque = bonusAtaque;
            this.bonusDefesa = bonusDefesa;
        }

        @Override
        public String getNome() { return nome + " (ATQ+" + bonusAtaque + "/DEF+" + bonusDefesa + ")"; }
        @Override
        public int getBonusAtaque() { return bonusAtaque; }
        @Override
        public int getBonusDefesa() { return bonusDefesa; }
    }

    public static class Inventario {
        private final List<ItemConsumivel> pocoes = new ArrayList<>();

        public void adicionarPocao(ItemConsumivel item) { pocoes.add(item); }
        public boolean temPocoes() { return !pocoes.isEmpty(); }
        public ItemConsumivel usarPrimeiraPocao() { return pocoes.remove(0); }
        public int quantidadePocoes() { return pocoes.size(); }
    }

    public static abstract class EntidadeBase implements Combatente {
        private final String nome;
        private int vida;
        private final int vidaMax;
        private final int ataque;
        private final int defesa;

        protected EntidadeBase(String nome, int vidaMax, int ataque, int defesa) {
            this.nome = nome;
            this.vidaMax = vidaMax;
            this.vida = vidaMax;
            this.ataque = ataque;
            this.defesa = defesa;
        }

        @Override public String getNome() { return nome; }
        @Override public int getVida() { return vida; }
        @Override public int getVidaMax() { return vidaMax; }
        @Override public int getAtaqueTotal() { return ataque; }
        @Override public int getDefesaTotal() { return defesa; }
        @Override public boolean estaVivo() { return vida > 0; }

        @Override
        public int receberDano(int dano) {
            int aplicado = Math.max(1, dano);
            this.vida = Math.max(0, this.vida - aplicado);
            return aplicado;
        }

        public int curar(int valor) {
            int antes = this.vida;
            this.vida = Math.min(this.vidaMax, this.vida + Math.max(0, valor));
            return this.vida - antes;
        }
    }

    public static abstract class Personagem extends EntidadeBase {
        private final Inventario inventario = new Inventario();
        private ItemEquipavel equipamento;

        protected Personagem(String nome, int vidaMax, int ataque, int defesa) {
            super(nome, vidaMax, ataque, defesa);
        }

        public Inventario getInventario() { return inventario; }

        public void equipar(ItemEquipavel novo, InterfaceUsuario ui) {
            this.equipamento = novo;
            ui.mostrar(" [EQUIPADO] " + getNome() + " equipou " + novo.getNome());
        }

        @Override
        public int getAtaqueTotal() {
            return super.getAtaqueTotal() + (equipamento != null ? equipamento.getBonusAtaque() : 0);
        }

        @Override
        public int getDefesaTotal() {
            return super.getDefesaTotal() + (equipamento != null ? equipamento.getBonusDefesa() : 0);
        }

        @Override
        public void atacar(Combatente alvo, RegraDano regra, InterfaceUsuario ui) {
            int dano = alvo.receberDano(regra.calcular(getAtaqueTotal(), alvo.getDefesaTotal()));
            ui.mostrar(" [ATAQUE] " + getNome() + " causou " + dano + " de dano em " + alvo.getNome() + "!");
        }

        // [POO - Polimorfismo] Cada classe implementa sua própria habilidade especial
        public abstract String getNomeHabilidade();
        public abstract void usarHabilidade(Combatente alvo, RegraDano regra, InterfaceUsuario ui);
    }

    public static class Guerreiro extends Personagem {
        public Guerreiro(String nome) { super(nome, 130, 22, 14); }

        @Override public String getNomeHabilidade() { return "Golpe Demolidor (150% ATQ)"; }

        @Override
        public void usarHabilidade(Combatente alvo, RegraDano regra, InterfaceUsuario ui) {
            int dano = alvo.receberDano(regra.calcular((int) (getAtaqueTotal() * 1.5), alvo.getDefesaTotal()));
            ui.mostrar(" [HABILIDADE] " + getNome() + " usou Golpe Demolidor e causou " + dano + " de dano!");
        }
    }

    public static class Mago extends Personagem {
        public Mago(String nome) { super(nome, 95, 28, 8); }

        @Override public String getNomeHabilidade() { return "Tempestade Arcana (Ignora Defesa + Cura 10 HP)"; }

        @Override
        public void usarHabilidade(Combatente alvo, RegraDano regra, InterfaceUsuario ui) {
            int dano = alvo.receberDano(getAtaqueTotal() + 6);
            curar(10);
            ui.mostrar(" [HABILIDADE] " + getNome() + " lançou Tempestade Arcana causando " + dano + " de dano puro e curando 10 HP!");
        }
    }

    public static class Arqueiro extends Personagem {
        public Arqueiro(String nome) { super(nome, 110, 24, 10); }

        @Override public String getNomeHabilidade() { return "Flecha Dupla (2 Disparos)"; }

        @Override
        public void usarHabilidade(Combatente alvo, RegraDano regra, InterfaceUsuario ui) {
            int d1 = alvo.receberDano(regra.calcular(getAtaqueTotal(), alvo.getDefesaTotal() / 2));
            int d2 = alvo.estaVivo() ? alvo.receberDano(regra.calcular(getAtaqueTotal(), alvo.getDefesaTotal() / 2)) : 0;
            ui.mostrar(" [HABILIDADE] " + getNome() + " disparou Flecha Dupla causando " + (d1 + d2) + " de dano total!");
        }
    }

    public static class Inimigo extends EntidadeBase {
        private final ItemEquipavel recompensa;

        public Inimigo(String nome, int vidaMax, int ataque, int defesa, ItemEquipavel recompensa) {
            super(nome, vidaMax, ataque, defesa);
            this.recompensa = recompensa;
        }

        public ItemEquipavel getRecompensa() { return recompensa; }

        @Override
        public void atacar(Combatente alvo, RegraDano regra, InterfaceUsuario ui) {
            int dano = alvo.receberDano(regra.calcular(getAtaqueTotal(), alvo.getDefesaTotal()));
            ui.mostrar(" [INIMIGO] " + getNome() + " atacou causando " + dano + " de dano!");
        }
    }

    public static class Chefao extends Inimigo {
        private int turno = 0;

        public Chefao(String nome, int vidaMax, int ataque, int defesa) {
            super(nome, vidaMax, ataque, defesa, null);
        }

        @Override
        public void atacar(Combatente alvo, RegraDano regra, InterfaceUsuario ui) {
            turno++;
            if (turno % 2 == 0 || getVida() <= getVidaMax() / 2) {
                int dano = alvo.receberDano(regra.calcular(getAtaqueTotal() + 10, alvo.getDefesaTotal()));
                ui.mostrar(" [CHEFÃO ESPECIAL] " + getNome() + " lançou Sopro Vulcânico causando " + dano + " de dano!");
            } else {
                super.atacar(alvo, regra, ui);
            }
        }
    }

    public static class MotorDeCombate {
        private final InterfaceUsuario ui;
        private final RegraDano regraDano;

        public MotorDeCombate(InterfaceUsuario ui, RegraDano regraDano) {
            this.ui = ui;
            this.regraDano = regraDano;
        }

        public boolean batalhar(Personagem heroi, Inimigo inimigo) {
            ui.mostrar("\n--- BATALHA: " + heroi.getNome() + " VS " + inimigo.getNome() + " ---");
            while (heroi.estaVivo() && inimigo.estaVivo()) {
                ui.mostrar(String.format("\n[%s HP: %d/%d | ATQ: %d | DEF: %d] vs [%s HP: %d/%d]",
                        heroi.getNome(), heroi.getVida(), heroi.getVidaMax(),
                        heroi.getAtaqueTotal(), heroi.getDefesaTotal(),
                        inimigo.getNome(), inimigo.getVida(), inimigo.getVidaMax()));
                ui.mostrar("1-Atacar | 2-" + heroi.getNomeHabilidade()
                        + " | 3-Usar Poção (" + heroi.getInventario().quantidadePocoes() + ")");

                int acao = ui.lerOpcao(1, 3);
                if (acao == 1) {
                    heroi.atacar(inimigo, regraDano, ui);
                } else if (acao == 2) {
                    heroi.usarHabilidade(inimigo, regraDano, ui);
                } else if (heroi.getInventario().temPocoes()) {
                    heroi.getInventario().usarPrimeiraPocao().usar(heroi, ui);
                } else {
                    ui.mostrar(" [!] Sem poções no inventário! Escolha outra ação.");
                    continue;
                }

                if (!inimigo.estaVivo()) {
                    ui.mostrar(" [VITÓRIA] " + inimigo.getNome() + " foi derrotado!");
                    if (inimigo.getRecompensa() != null) {
                        heroi.equipar(inimigo.getRecompensa(), ui);
                    }
                    return true;
                }
                inimigo.atacar(heroi, regraDano, ui);
            }
            return false;
        }
    }
}