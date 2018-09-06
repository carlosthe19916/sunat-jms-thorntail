package io.github.carlosthe19916.wildflyswarm.sender.service;

import io.github.carlosthe19916.wildflyswarm.sender.qualifiers.JMSunatContext;
import io.github.carlosthe19916.wildflyswarm.sender.qualifiers.JMSunatQueue;
import io.github.carlosthe19916.sender.model.JMSBillConsultMessageBean;
import io.github.carlosthe19916.sender.model.JMSSenderConfig;
import io.github.carlosthe19916.sender.model.SenderConstants;
import io.github.carlosthe19916.sender.service.JMSBillConsultService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.*;

@ApplicationScoped
public class DefaultBillConsultService implements JMSBillConsultService {

    @Inject
    @JMSunatContext
    private JMSContext context;

    @Inject
    @JMSunatQueue
    private Queue queue;

    @Override
    public void getStatus(JMSSenderConfig config, JMSBillConsultMessageBean bean) throws JMSException {
        consult(config, bean, SenderConstants.GET_STATUS);
    }

    @Override
    public void getStatusCdr(JMSSenderConfig config, JMSBillConsultMessageBean bean) throws JMSException {
        consult(config, bean, SenderConstants.GET_STATUS_CDR);
    }

    private void consult(JMSSenderConfig config, JMSBillConsultMessageBean bean, String operation) throws JMSException {
        MapMessage message = context.createMapMessage();
        message.setString(SenderConstants.RUC_COMPROBANTE, bean.getRucComprobante());
        message.setString(SenderConstants.TIPO_COMPROBANTE, bean.getTipoComprobante());
        message.setString(SenderConstants.SERIE_COMPROBANTE, bean.getSerieComprobante());
        message.setInt(SenderConstants.NUMERO_COMPROBANTE, bean.getNumeroComprobante());

        message.setStringProperty(SenderConstants.DESTINY, config.getEndpoint());
        message.setStringProperty(SenderConstants.DESTINY_OPERATION, operation);

        message.setStringProperty(SenderConstants.SENDER_USERNAME, config.getUsername());
        message.setStringProperty(SenderConstants.SENDER_PASSWORD, config.getPassword());

        JMSProducer producer = context.createProducer();
        producer.send(queue, message);
    }

}
