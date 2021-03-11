<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="label">
        <h2 class="jh-entity-heading" data-cy="labelDetailsHeading"><span>Label</span> {{ label.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Label</span>
          </dt>
          <dd>
            <span>{{ label.label }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="label.id" :to="{ name: 'LabelEdit', params: { labelId: label.id } }" tag="button" class="btn btn-primary">
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./label-details.component.ts"></script>
