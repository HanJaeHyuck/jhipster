<template>
  <div>
    <h2 id="page-heading" data-cy="TicketHeading">
      <span id="ticket-heading">Tickets</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link
          :to="{ name: 'TicketCreate' }"
          tag="button"
          id="jh-create-entity"
          data-cy="entityCreateButton"
          class="btn btn-primary jh-create-entity create-ticket"
        >
          <font-awesome-icon icon="plus"></font-awesome-icon>
          <span> Create a new Ticket </span>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && tickets && tickets.length === 0">
      <span>No tickets found</span>
    </div>
    <div class="table-responsive" v-if="tickets && tickets.length > 0">
      <table class="table table-striped" aria-describedby="tickets">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('title')">
              <span>Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span>Description</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('dueDate')">
              <span>Due Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dueDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('date')">
              <span>Date</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'date'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('status')">
              <span>Status</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('type')">
              <span>Type</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'type'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('priority')">
              <span>Priority</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priority'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('project.name')">
              <span>Project</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'project.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('assignedTo.login')">
              <span>Assigned To</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'assignedTo.login'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('reportedBy.login')">
              <span>Reported By</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reportedBy.login'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ticket in tickets" :key="ticket.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TicketView', params: { ticketId: ticket.id } }">{{ ticket.id }}</router-link>
            </td>
            <td>{{ ticket.title }}</td>
            <td>{{ ticket.description }}</td>
            <td>{{ ticket.dueDate }}</td>
            <td>{{ ticket.date | formatDate }}</td>
            <td>{{ ticket.status }}</td>
            <td>{{ ticket.type }}</td>
            <td>{{ ticket.priority }}</td>
            <td>
              <div v-if="ticket.project">
                <router-link :to="{ name: 'ProjectView', params: { projectId: ticket.project.id } }">{{ ticket.project.name }}</router-link>
              </div>
            </td>
            <td>
              {{ ticket.assignedTo ? ticket.assignedTo.login : '' }}
            </td>
            <td>
              {{ ticket.reportedBy ? ticket.reportedBy.login : '' }}
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'TicketView', params: { ticketId: ticket.id } }"
                  tag="button"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline">View</span>
                </router-link>
                <router-link
                  :to="{ name: 'TicketEdit', params: { ticketId: ticket.id } }"
                  tag="button"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline">Edit</span>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(ticket)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="jh001App.ticket.delete.question" data-cy="ticketDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-ticket-heading">Are you sure you want to delete this Ticket?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-ticket"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeTicket()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="tickets && tickets.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ticket.component.ts"></script>
