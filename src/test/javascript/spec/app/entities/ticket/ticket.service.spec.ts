/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from '@/shared/date/filters';
import TicketService from '@/entities/ticket/ticket.service';
import { Ticket } from '@/shared/model/ticket.model';
import { Status } from '@/shared/model/enumerations/status.model';
import { Type } from '@/shared/model/enumerations/type.model';
import { Priority } from '@/shared/model/enumerations/priority.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Ticket Service', () => {
    let service: TicketService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new TicketService();
      currentDate = new Date();
      elemDefault = new Ticket(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, Status.OPEN, Type.BUG, Priority.HIGHEST);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            dueDate: dayjs(currentDate).format(DATE_FORMAT),
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Ticket', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dueDate: dayjs(currentDate).format(DATE_FORMAT),
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dueDate: currentDate,
            date: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Ticket', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Ticket', async () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            dueDate: dayjs(currentDate).format(DATE_FORMAT),
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
            status: 'BBBBBB',
            type: 'BBBBBB',
            priority: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate,
            date: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Ticket', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Ticket', async () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            dueDate: dayjs(currentDate).format(DATE_FORMAT),
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
            status: 'BBBBBB',
            type: 'BBBBBB',
            priority: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dueDate: currentDate,
            date: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Ticket', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Ticket', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Ticket', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
