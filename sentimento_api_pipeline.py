# Importar as ferramentas necessarias
from flask import Flask, request, jsonify
from werkzeug.exceptions import HTTPException
import joblib
import logging
import sys

# Configuração básica de logs no terminal
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Inicialização do app 
app = Flask(__name__)

# Carrega o modelo de treinamento
PIPELINE_PATH = (r"modelo_sentimento_final.joblib")
pipeline = None # Inicializa a variável do pipeline

try:
    # O pipeline é carregado uma única vez ao iniciar o servidor
    pipeline = joblib.load(PIPELINE_PATH) 
    print(f"Pipeline ML carregado com sucesso de: {PIPELINE_PATH}")
except Exception as e:
    # Se o carregamento falhar (arquivo não encontrado, corrompido, etc.), 
    # o erro é impresso e o programa encerra.
    print(f"ERRO FATAL: Falha ao carregar o pipeline: {e}")
    sys.exit(1)

# Tratamento de erros
@app.errorhandler(Exception)
def handle_unexpected_error(e):
    #Captura qualquer erro não tratado e retorna um JSON padronizado.
    # Se for um erro de rota (404) ou método não permitido (405)
    if isinstance(e, HTTPException):
        return jsonify({
            "success": False,
            "error": e.name,
            "message": e.description
        }), e.code

    # Erros internos de lógica ou do modelo (500)
    logger.error(f"Erro interno: {str(e)}")
    return jsonify({
        "success": False,
        "error": "Internal Server Error",
        "message": "Ocorreu um erro inesperado no processamento do sentimento."
    }), 500

# Rotas da API/Endpoint 
@app.route('/sentiment', methods=['POST'])
def analyze_sentiment():
    # Checagem de disponibilidade do modelo
    if pipeline is None:
        # Retorna erro 503 se o modelo não foi carregado
        return jsonify({"error": "Serviço indisponível: Modelo não carregado."}), 503

    # Validação 1: O corpo da requisição é um JSON?
    data = request.get_json(silent=True)
    if data is None:
        return jsonify({
            "success": False, 
            "error": "Bad Request", 
            "message": "O corpo da requisição deve ser um JSON válido."
        }), 400

    # Validação 2: A chave 'comentario' existe e tem conteúdo?
    comment = data.get('comentario','').strip()
    if not comment:
        return jsonify({
            "success": False, 
            "error": "Unprocessable Entity", 
            "message": "O campo 'comentario' é obrigatório e não pode estar vazio."
        }), 422

    try:
        # INFERÊNCIA COM PIPELINE
        # O pipeline.predict() e .predict_proba() aceitam o texto bruto
        # e fazem a vetorização automaticamente internamente.
        
        # Faz a predição (classe)
        prediction = pipeline.predict([comment])[0]
        
        # Faz a predição (probabilidades)
        probabilities = pipeline.predict_proba([comment])[0]  
        
        #Usamos list() para garantir compatibilidade, pois pipeline.classes_ é um ndarray
        probability_value = probabilities[list(pipeline.classes_).index(prediction)]
        
        # Retorna a resposta (formato esperado pelo DTO Java)
        response = {
            "comentario": comment,
            # Garante que a saída seja string (essencial para JSON)
            "sentimento": str(prediction), 
            "probabilidade": round(float(probability_value), 4)
        }
        
        return jsonify(response), 200

    except Exception as e:
        # Erro genérico durante a predição
        print(f"Erro durante a predição: {e}")
        return jsonify({"error": "Erro interno durante a análise de sentimento"}), 500

if __name__ == '__main__':
    # Utilizar um servidor WSGI de produção (como Gunicorn) para deploy final.
    # Para testes locais:
    app.run(host='127.0.0.1', port=5000, debug=True)