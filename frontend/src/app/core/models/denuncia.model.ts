export enum Tipologia {
  FALSIFICACAO_DOCUMENTO = 'FALSIFICACAO_DOCUMENTO',
  FATIAMENTO_PEDIDOS = 'FATIAMENTO_PEDIDOS',
  CAPTACAO_ILICITA_CLIENTELA = 'CAPTACAO_ILICITA_CLIENTELA',
  ACAO_SEM_CONSENTIMENTO = 'ACAO_SEM_CONSENTIMENTO',
  TESE_ARTIFICIAL_MASSA = 'TESE_ARTIFICIAL_MASSA',
  OUTROS = 'OUTROS'
}

export const TIPOLOGIA_LABELS: Record<Tipologia, string> = {
  [Tipologia.FALSIFICACAO_DOCUMENTO]: 'Falsificação ou adulteração de documentos',
  [Tipologia.FATIAMENTO_PEDIDOS]: 'Ajuizamento de múltiplas ações idênticas fragmentadas',
  [Tipologia.CAPTACAO_ILICITA_CLIENTELA]: 'Captação ilícita de clientela',
  [Tipologia.ACAO_SEM_CONSENTIMENTO]: 'Ações propostas sem consentimento/conhecimento real da parte',
  [Tipologia.TESE_ARTIFICIAL_MASSA]: 'Uso de teses artificiais ou fabricadas em massa',
  [Tipologia.OUTROS]: 'Outros'
};

export enum StatusDenuncia {
  NOVO = 'NOVO',
  EM_ANALISE_NUMOPED = 'EM_ANALISE_NUMOPED',
  AGUARDANDO_NOTA_TECNICA = 'AGUARDANDO_NOTA_TECNICA',
  EM_PROVIDENCIA_CORREGEDORIA = 'EM_PROVIDENCIA_CORREGEDORIA',
  ARQUIVADO = 'ARQUIVADO'
}

export const STATUS_LABELS: Record<StatusDenuncia, string> = {
  [StatusDenuncia.NOVO]: 'Novo',
  [StatusDenuncia.EM_ANALISE_NUMOPED]: 'Em Análise pelo NUMOPED',
  [StatusDenuncia.AGUARDANDO_NOTA_TECNICA]: 'Aguardando Nota Técnica',
  [StatusDenuncia.EM_PROVIDENCIA_CORREGEDORIA]: 'Em Providência na Corregedoria',
  [StatusDenuncia.ARQUIVADO]: 'Arquivado'
};

export const STATUS_ORDER: StatusDenuncia[] = [
  StatusDenuncia.NOVO,
  StatusDenuncia.EM_ANALISE_NUMOPED,
  StatusDenuncia.AGUARDANDO_NOTA_TECNICA,
  StatusDenuncia.EM_PROVIDENCIA_CORREGEDORIA,
  StatusDenuncia.ARQUIVADO
];

export interface DenunciaRequest {
  comarca: string;
  nomeComunicante: string;
  matriculaComunicante: string;
  oabAdvogado: string;
  cpfCnpjParte?: string;
  tipologia: Tipologia | null;
  tipologiaOutros?: string;
  processosParadigmas: string[];
  relato: string;
  anexos: string[];
}

export interface DenunciaResponse {
  id: number;
  versao: number;
  historico: HistoricoItem[];
  numeroProtocolo: string;
  comarca: string;
  nomeComunicante: string;
  matriculaComunicante: string;
  oabAdvogado: string;
  cpfCnpjParte?: string;
  tipologia: Tipologia;
  tipologiaDescricao: string;
  tipologiaOutros?: string;
  processosParadigmas: string[];
  relato: string;
  anexos: string[];
  status: StatusDenuncia;
  statusDescricao: string;
  volumeProcessualEstimado?: number;
  comarcasAtingidas?: number;
  notaTecnicaResumo?: string;
  dataCriacao: string;
  dataAtualizacao: string;
}

export interface HistoricoItem {
  id: number;
  statusAnterior: StatusDenuncia | null;
  statusNovo: StatusDenuncia;
  autor: string;
  data: string;
  volume: number | null;
  comarcas: number | null;
  nota: string | null;
}

export interface PaginaDenuncias {
  itens: DenunciaResponse[];
  total: number;
  pagina: number;
  totalPaginas: number;
  totaisPorStatus: Partial<Record<StatusDenuncia, number>>;
}

export interface StatusUpdateRequest {
  versao: number;
  status: StatusDenuncia;
  volumeProcessualEstimado?: number;
  comarcasAtingidas?: number;
  notaTecnicaResumo: string;
  limparVolume: boolean;
  limparComarcas: boolean;
}

export interface KpisResponse {
  totalDenuncias: number;
  volumeProcessualAfetado: number;
  comarcasAfetadas: number;
}

export interface RankingItem {
  identificador: string;
  totalDenuncias: number;
  comarcasEnvolvidas: number;
}

export interface TipologiaItem {
  tipologia: string;
  descricao: string;
  total: number;
  percentual: number;
}

export interface ComarcaItem {
  comarca: string;
  total: number;
}

export interface TendenciaItem {
  semana: string;
  total: number;
}
