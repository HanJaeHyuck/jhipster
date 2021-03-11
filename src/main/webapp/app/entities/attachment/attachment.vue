<template>
  <div>
    <h2 id="page-heading" data-cy="AttachmentHeading">
      <span id="attachment-heading">Attachments</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link
          :to="{ name: 'AttachmentCreate' }"
          tag="button"
          id="jh-create-entity"
          data-cy="entityCreateButton"
          class="btn btn-primary jh-create-entity create-attachment"
        >
          <font-awesome-icon icon="plus"></font-awesome-icon>
          <span> Create a new Attachment </span>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && attachments && attachments.length === 0">
      <span>No attachments found</span>
    </div>
    <div class="table-responsive" v-if="attachments && attachments.length > 0">
      <table class="table table-striped" aria-describedby="attachments">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span>Name</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('file')">
              <span>File</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'file'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('ticket.id')">
              <span>Ticket</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ticket.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="attachment in attachments" :key="attachment.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AttachmentView', params: { attachmentId: attachment.id } }">{{ attachment.id }}</router-link>
            </td>
            <td>{{ attachment.name }}</td>
            <td>
              <a v-if="attachment.file" v-on:click="openFile(attachment.fileContentType, attachment.file)">open</a>
              <span v-if="attachment.file">{{ attachment.fileContentType }}, {{ byteSize(attachment.file) }}</span>
            </td>
            <td>
              <div v-if="attachment.ticket">
                <router-link :to="{ name: 'TicketView', params: { ticketId: attachment.ticket.id } }">{{
                  attachment.ticket.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'AttachmentView', params: { attachmentId: attachment.id } }"
                  tag="button"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline">View</span>
                </router-link>
                <router-link
                  :to="{ name: 'AttachmentEdit', params: { attachmentId: attachment.id } }"
                  tag="button"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline">Edit</span>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(attachment)"
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
        ><span id="jh001App.attachment.delete.question" data-cy="attachmentDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-attachment-heading">Are you sure you want to delete this Attachment?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-attachment"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeAttachment()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./attachment.component.ts"></script>
