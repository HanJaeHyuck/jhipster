<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="comment">
        <h2 class="jh-entity-heading" data-cy="commentDetailsHeading"><span>Comment</span> {{ comment.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Date</span>
          </dt>
          <dd>
            <span>{{ comment.date | formatDate }}</span>
          </dd>
          <dt>
            <span>Text</span>
          </dt>
          <dd>
            <span>{{ comment.text }}</span>
          </dd>
          <dt>
            <span>Login</span>
          </dt>
          <dd>
            {{ comment.login ? comment.login.id : '' }}
          </dd>
          <dt>
            <span>Child</span>
          </dt>
          <dd>
            <div v-if="comment.child">
              <router-link :to="{ name: 'CommentView', params: { commentId: comment.child.id } }">{{ comment.child.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="comment.id"
          :to="{ name: 'CommentEdit', params: { commentId: comment.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./comment-details.component.ts"></script>
